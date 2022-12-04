package org.agency.controller;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Ticket;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.service.auth.AuthService;
import org.agency.service.manager.ManagerService;
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
    private final AuthService authService;
    private final ManagerService managerService;

    public ManagerController(ServiceDelegator serviceDelegator) {
        this.managerService = (ManagerService) serviceDelegator.getByClass(ManagerService.class);
        this.userService = (UserService) serviceDelegator.getByClass(UserService.class);
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    public void assignMasterToTicket(Long ticketId, String masterEmail) {
        try {
            this.ticketService.assignMaster(ticketId, masterEmail);
            logger.info(String.format("Successfully assigned master [email=%s] to ticket [id=%d]", masterEmail, ticketId));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't assign master to the ticket, see: ");
        }
    }

    public Ticket setTicketPrice(Long ticketId, BigDecimal price) {
        try {
            return this.ticketService.updatePrice(ticketId, price);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
            return null;
        }
    }

    public void setStatus(Long ticketId, String updatedStatus) {
        try {
            this.managerService.updateStatus(ticketId, updatedStatus);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

    public void topUpAccount(String email, BigDecimal amount) {
        try {
            this.userService.topUpBalance(email, amount);
            logger.info(String.format("Balance of user with email=[%s] was topped up with amount=[%f]", email, amount));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
        }
    }

    public void logout() {
        this.authService.logout();
    }

    public List<Ticket> getTickets() {
        return this.ticketService.getAll();
    }

    public List<Ticket> getSortedByDate() {
        return this.ticketService.getSortedByDate();
    }

    public List<Ticket> getSortedByStatus() {
        return this.ticketService.getSortedByStatus();
    }

    public List<Ticket> getSortedByPrice() {
        return this.ticketService.getSortedByPrice();
    }

    public List<Ticket> getFilteredByMaster(String masterEmail) {
        return this.ticketService.getFilteredByMaster(masterEmail);
    }

    // TODO when do user pay for the request/ticket? after ticket will be successfully done?

}
