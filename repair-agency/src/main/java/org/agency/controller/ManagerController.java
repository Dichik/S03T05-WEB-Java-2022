package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.exception.TicketNotFoundException;
import org.agency.exception.UserNotFoundException;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

public class ManagerController {
    private static final Logger logger = LogManager.getLogger(ManagerController.class);

    private final UserService userService;
    private final TicketService ticketService;

    public ManagerController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    public List<Ticket> getFilteredTickets() { // FIXME should be generic. SOLID
        // TODO implement method
        return null;
    }

    public void assignMasterToTicket(Long ticketId, Long masterId) {
        try {
            this.ticketService.assignMaster(ticketId, masterId);
            logger.info(String.format("Successfully assigned master [id=%d] to ticket [id=%d]", masterId, ticketId));
        } catch (TicketNotFoundException e) {
            logger.error("Couldn't assign master to the ticket, see: ");
        }
    }

    public Ticket setTicketPrice(Long ticketId, BigDecimal price) {
        try {
            return this.ticketService.updatePrice(ticketId, price);
        } catch (TicketNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
            return null;
        }
    }

    public void setStatus(Long ticketId, String updatedStatus) { // TODO create enum for statuses
        try {
            this.ticketService.updateStatus(ticketId, updatedStatus);
            logger.info(String.format("Successfully set %s status for ticket with %d id", updatedStatus, ticketId));
        } catch (TicketNotFoundException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

    public void topUpAccount(String email, BigDecimal amount) {
        try {
            this.userService.topUpBalance(email, amount);
            logger.info(String.format("Manager %s successfully topped up %s account. Current balance: %a", "Manager1", email, 0.0));
        } catch (UserNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
        }
    }

    // TODO when do user pay for the request/ticket? after ticket will be successfully done?

}
