package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.service.manager.ManagerService;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/managers")
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

    @GetMapping
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
        return this.ticketService.getFilteredByStatus(status);
    }

    @PostMapping
    public void assignMasterToTicket(@RequestParam Long ticketId, @RequestParam String masterEmail) {
        try {
            this.ticketService.assignMaster(ticketId, masterEmail);
            logger.info(String.format("Successfully assigned master [email=%s] to ticket [id=%d]", masterEmail, ticketId));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't assign master to the ticket, see: ");
        }
    }

    @PostMapping
    public void setTicketPrice(@RequestParam Long ticketId, @RequestParam BigDecimal price) {
        try {
            this.ticketService.updatePrice(ticketId, price);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
        }
    }

    @PostMapping
    public void setStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus) {
        try {
            this.managerService.updateStatus(ticketId, updatedStatus);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

    @PostMapping
    public void topUpAccount(@RequestParam String email, @RequestParam BigDecimal amount) {
        try {
            this.userService.topUpBalance(email, amount);
            logger.info(String.format("Balance of user with email=[%s] was topped up with amount=[%f]", email, amount));
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
        }
    }

}
