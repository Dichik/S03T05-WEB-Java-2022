package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.session.CurrentSession;
import org.agency.service.session.Session;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
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

    @PostMapping
    public void createTicket(@RequestBody @Valid Ticket ticket) {
        this.ticketService.createTicket(ticket);
    }

    @PostMapping
    public void leaveFeedback(@RequestParam Long ticketId, @RequestParam String feedbackText) {
        Optional<Ticket> ticket = this.ticketService.getById(ticketId);
        if (!ticket.isPresent() || ticket.get().getStatus() != TicketStatus.DONE) {
            return;
        }
        this.feedbackService.submit(ticketId, feedbackText);
    }

    @GetMapping
    public List<Ticket> getTicketsByUserEmail(@RequestParam String email) {
        return this.ticketService.getTicketsByUserEmail(email);
    }

    @GetMapping("/balance")
    public BigDecimal getCurrentBalance() {
        Session session = CurrentSession.getSession();
        try {
            Optional<User> user = this.userService.findByEmail(session.getEmail());
            if (!user.isPresent()) {
                logger.warn(String.format("User with email=[%s] was not found.", session.getEmail()));
                return BigDecimal.ZERO;
            }
            return user.get().getBalance();
        } catch (EntityNotFoundException e) {
            logger.error(String.format("Couldn't get current balance for user=[%s], see: %s", session.getEmail(), e));
            return BigDecimal.ZERO;
        }
    }

    @PostMapping
    public void payForTicket(@RequestParam Long ticketId, @RequestParam String userEmail) {
        try {
            this.userService.payForTicket(ticketId, userEmail);
        } catch (EntityNotFoundException e) {
            logger.warn(String.format("User with email=[%s] couldn't pay for ticket with id=[%d]", userEmail, ticketId));
        }
    }

}
