package org.agency.controller;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Ticket;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.service.auth.AuthService;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.session.CurrentSession;
import org.agency.service.session.Session;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final TicketService ticketService;
    private final FeedbackService feedbackService;
    private final AuthService authService;

    public UserController(ServiceDelegator serviceDelegator) {
        this.userService = (UserService) serviceDelegator.getByClass(UserService.class);
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.feedbackService = (FeedbackService) serviceDelegator.getByClass(FeedbackService.class);
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    // TODO research how to perform one method before execution another (like filter?)

    public void createTicket(Ticket ticket) {
        this.ticketService.createTicket(ticket);
    }

    // TODO we should have option to check notifications
    // and one of them should contains "Feedback about solving ticket #3: fix laptop"
    public void leaveFeedback(Long ticketId, String feedbackText) {
        // TODO check from which user we perform operation
        // TODO create controller to check authentification
        if (!this.ticketService.ticketExistsById(ticketId)) {
            return;
        }
        this.feedbackService.submit(ticketId, feedbackText);
    }

    public List<Ticket> getTicketsByUserEmail(String email) {
        return this.ticketService.getTicketsByUserEmail(email);
    }

    public void logout() {
        this.authService.logout();
    }

    public BigDecimal getCurrentBalance() {
        Session session = CurrentSession.getSession();
        try {
            User user = this.userService.findByEmail(session.getEmail());
            return user.getBalance();
        } catch (EntityNotFoundException e) {
            logger.error(String.format("Couldn't get current balance for user=[%s], see: %s", session.getEmail(), e));
            return BigDecimal.ZERO;
        }
    }

}
