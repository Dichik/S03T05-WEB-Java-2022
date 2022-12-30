package com.agency.finalproject.controller;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.service.manager.ManagerService;
import com.agency.finalproject.service.ticket.TicketService;
import com.agency.finalproject.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

// FIXME fix this controller with Secured annotation and others

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/managers")
public class ManagerController {
    private static final Logger logger = LogManager.getLogger(ManagerController.class);

    private final UserService userService;
    private final TicketService ticketService;
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService, UserService userService, TicketService ticketService) {
        this.managerService = managerService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(method = RequestMethod.GET)
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

    public List<Ticket> getFilterByStatus(String status) {
        return this.ticketService.getFilteredByStatus(TicketStatus.valueOf(status));
    }

    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "masterEmail"})
    public void assignMasterToTicket(@RequestParam Long ticketId, @RequestParam String masterEmail) {
        try {
            this.ticketService.assignMaster(ticketId, masterEmail);
            logger.info(String.format("Successfully assigned master [email=%s] to ticket [id=%d]", masterEmail, ticketId));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't assign master to the ticket, see: ");
        }
    }

    // TODO can we do all that 'set's as PUT/PATCH operation?
    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "price"})
    public void setTicketPrice(@RequestParam Long ticketId, @RequestParam BigDecimal price) {
        try {
            this.ticketService.updatePrice(ticketId, price);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "updatedStatus"})
    public void setStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus) {
        try {
            this.managerService.updateStatus(ticketId, updatedStatus);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

    // TODO do we really need this method here?
    @RequestMapping(method = RequestMethod.POST, params = {"email", "amount"})
    public void topUpAccount(@RequestParam String email, @RequestParam BigDecimal amount) {
        try {
            this.userService.topUpBalance(email, amount);
            logger.info(String.format("Balance of user with email=[%s] was topped up with amount=[%f]", email, amount));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
        }
    }

}
