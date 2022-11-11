package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.entity.User;
import org.agency.exception.UserNotFoundException;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class ManagerController {
    private static final Logger logger = LogManager.getLogger(ManagerController.class);

    private final UserService userService;
    private final TicketService ticketService;

    public ManagerController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    public void addMasterToTicket(Long masterId, Long ticketId) {
        // TODO implement addMasterToTicket() method
    }

    public BigDecimal calculateTicketCost(Long ticketId) {
        // TODO here we should calculate how much it costs to master to fix ticket
        return null;
    }

    public void setStatus(Long ticketId, String updatedStatus) { // TODO create enum for statuses
        this.ticketService.updateStatus(ticketId, updatedStatus);
    }

    public void topUpAccount(String userEmail, BigDecimal amount) throws UserNotFoundException {
        User user = this.userService.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException("User with " + userEmail + " email was not found");
        }
        BigDecimal currentBalance = user.topUp(amount);
        logger.info(String.format("Manager %s successfully topped up %s account. Current balance: %a", "Manager1", user.getEmail(), currentBalance));
        this.userService.update(user);
    }


}
