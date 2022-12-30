package com.agency.finalproject.controller;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.feedback.FeedbackService;
import com.agency.finalproject.service.ticket.TicketService;
import com.agency.finalproject.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final TicketService ticketService;
    private final FeedbackService feedbackService;

    @Autowired
    public UserController(UserService userService, TicketService ticketService, FeedbackService feedbackService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.feedbackService = feedbackService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(this.ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(method = RequestMethod.POST, value = "feedback", params = {"ticketId", "text"})
    public ResponseEntity<?> leaveFeedback(@RequestParam Long ticketId, @RequestParam String text,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Ticket> ticket = this.ticketService.getById(ticketId);
        if (ticket.isEmpty() || ticket.get().getStatus() != TicketStatus.DONE) {
            return new ResponseEntity<>(new MessageResponse("Couldn't leave your feedback, try again."), HttpStatus.BAD_REQUEST);
        }

        Feedback feedback = this.feedbackService.submit(userDetails.getUsername(), ticketId, text);
        Map<String, Object> body = new LinkedHashMap<>() {{
            put("data", feedback);
            put("message", "Feedback was successfully submitted!");
        }};
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getTicketsByUserEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getEmail();
        List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(email);
        if (tickets.isEmpty()) {
            logger.info("No tickets were found for email=" + email);
            return new ResponseEntity<>(new MessageResponse("No tickets were found."), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<?> getBalance(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            User user = this.userService.findById(userDetails.id())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=[%d] was not found.", userDetails.id())));
            return new ResponseEntity<>(new MessageResponse("Your current balance is: " + user.getBalance()), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String message = String.format("Couldn't get current balance for user_id=[%d]", userDetails.id());
            logger.error(message + ", see: " + e);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "userEmail"})
    public ResponseEntity<?> payForTicket(@RequestParam Long ticketId, @RequestParam String userEmail) {
        try {
            this.userService.payForTicket(ticketId, userEmail);
            return new ResponseEntity<>("Ticket was successfully paid!", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String message = String.format("User with email=[%s] couldn't pay for ticket with id=[%d]", userEmail, ticketId);
            logger.warn(message);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/topup", method = RequestMethod.POST, params = {"username", "amount"})
    public ResponseEntity<?> topUpAccount(@RequestParam String username, @RequestParam BigDecimal amount) {
        try {
            User user = this.userService.topUpBalance(username, amount);
            String message = String.format("Balance of user with username=[%s] was topped up with amount=[%f]", username, amount);

            Map<String, Object> body = new LinkedHashMap<>(){{
                put("data", user);
                put("message", message);
            }};
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
            return new ResponseEntity<>("Couldn't top up accont, please try again.", HttpStatus.NOT_FOUND);
        }
    }

}
