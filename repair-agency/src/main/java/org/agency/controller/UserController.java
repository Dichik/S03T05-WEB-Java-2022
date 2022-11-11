package org.agency.controller;

import org.agency.entity.Feedback;
import org.agency.entity.Ticket;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.user.TicketService;
import org.agency.service.user.UserService;

public class UserController {

    private final TicketService ticketService;
    private final FeedbackService feedbackService;

    public UserController(TicketService ticketService, FeedbackService feedbackService) {
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
        Feedback feedback = new Feedback(); // TODO add Builder pattern
        feedback.setTicketId(ticketId);
        feedback.setText(feedbackText);
        this.feedbackService.submit(feedback);
    }

}
