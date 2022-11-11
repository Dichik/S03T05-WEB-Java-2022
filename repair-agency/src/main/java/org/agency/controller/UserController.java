package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final TicketService ticketService;
    private final FeedbackService feedbackService;

    public UserController(UserService userService, TicketService ticketService, FeedbackService feedbackService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.feedbackService = feedbackService;
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

}
