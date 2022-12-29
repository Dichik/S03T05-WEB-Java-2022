package com.agency.finalproject.controller;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.service.feedback.FeedbackService;
import com.agency.finalproject.service.ticket.TicketService;
import com.agency.finalproject.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(this.ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "feedback", params = {"ticketId", "text"})
    public ResponseEntity<?> leaveFeedback(@RequestParam Long ticketId, @RequestParam String text) {
        Optional<Ticket> ticket = this.ticketService.getById(ticketId);
        if (ticket.isEmpty() || ticket.get().getStatus() != TicketStatus.DONE) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.feedbackService.submit(ticketId, text);
        return new ResponseEntity<>("Feedback was successfully submitted!", HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"email"})
    public ResponseEntity<List<Ticket>> getTicketsByUserEmail(@RequestParam String email) {
        List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(email);
        if (tickets.isEmpty()) {
            logger.info("No tickes were found for email=" + email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    // FIXME get by email or filter (Spring Security)?
    @RequestMapping(value = "/{id:\\d+}/balance", method = RequestMethod.GET)
    public ResponseEntity<?> getBalance(/*@ApplicationPrincipal*/ @PathVariable Long id) {
        try {
            User user = this.userService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=[%d] was not found.", id)));
            return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String message = String.format("Couldn't get current balance for user_id=[%d]", id);
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

}
