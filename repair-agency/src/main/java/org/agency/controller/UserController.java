package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.service.user.TicketService;
import org.agency.service.user.UserService;

public class UserController {

    private final UserService userService;
    private final TicketService ticketService;

    public UserController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    // TODO research how to perform one method before execution another (like filter?)

    public void createTicket(Ticket ticket) {
        this.ticketService.createTicket(ticket);
    }

    // TODO we should have option to check notifications
    // and one of them should contains "Feedback about solving ticket #3: fix laptop"
    public void leaveFeedback(Long id) {
        // TODO check from which user we perform operation
        // TODO create controller to check authentification
    }

}
