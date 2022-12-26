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
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
    public void create(@RequestBody Ticket ticket) {
        this.ticketService.createTicket(ticket);
    }

    @RequestMapping(method = RequestMethod.POST, value = "feedback", params = {"ticketId", "text"})
    public void leaveFeedback(@RequestParam Long ticketId, @RequestParam String text) {
        Optional<Ticket> ticket = this.ticketService.getById(ticketId);
        if (ticket.isEmpty() || ticket.get().getStatus() != TicketStatus.DONE) {
            return;
        }
        this.feedbackService.submit(ticketId, text);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"email"})
    public List<Ticket> getTicketsByUserEmail(@RequestParam String email) {
        return this.ticketService.getTicketsByUserEmail(email);
    }

    // FIXME get by email or filter (Spring Security)?
    @RequestMapping(method = RequestMethod.GET, value = "/balance")
    public BigDecimal getCurrentBalance() {
        try {
            Optional<User> user = this.userService.findByEmail("email");
            if (user.isEmpty()) {
                logger.warn(String.format("User with email=[%s] was not found.", ""));
                return BigDecimal.ZERO;
            }
            return user.get().getBalance();
        } catch (EntityNotFoundException e) {
            logger.error(String.format("Couldn't get current balance for user=[%s], see: %s", "session.getEmail()", e));
            return BigDecimal.ZERO;
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "userEmail"})
    public void payForTicket(@RequestParam Long ticketId, @RequestParam String userEmail) {
        try {
            this.userService.payForTicket(ticketId, userEmail);
        } catch (EntityNotFoundException e) {
            logger.warn(String.format("User with email=[%s] couldn't pay for ticket with id=[%d]", userEmail, ticketId));
        }
    }

}
