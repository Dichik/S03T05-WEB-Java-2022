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
import java.util.List;
import java.util.Optional;

public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final TicketService ticketService;
    private final FeedbackService feedbackService;
    private final AuthService authService;

    public UserController(ServiceDelegator serviceDelegator) throws ClassNotFoundException {
        this.userService = (UserService) serviceDelegator.getByClass(UserService.class);
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.feedbackService = (FeedbackService) serviceDelegator.getByClass(FeedbackService.class);
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    public void createTicket(Ticket ticket) {
        this.ticketService.createTicket(ticket);
    }


    /**
     * TODO we should have option to check notifications
     *
     * TODO check from which user we perform operation
     *
     * TODO check if ticket in DONE phase
     */
    public void leaveFeedback(Long ticketId, String feedbackText) {
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

    public void payForTicket(Long ticketId, String userEmail) {
        try {
            this.userService.payForTicket(ticketId, userEmail);
        } catch (EntityNotFoundException e) {
            logger.warn(String.format("User with email=[%s] couldn't pay for ticket with id=[%d]", userEmail, ticketId));
        }
    }

}
