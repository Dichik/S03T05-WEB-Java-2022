package com.agency.finalproject.controller;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.service.master.MasterService;
import com.agency.finalproject.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/masters")
public class MasterController {
    private static final Logger logger = LogManager.getLogger(MasterController.class);

    private final TicketService ticketService;
    private final MasterService masterService;

    @Autowired
    public MasterController(TicketService ticketService, MasterService masterService) {
        this.ticketService = ticketService;
        this.masterService = masterService;
    }

    @RolesAllowed("MANAGER")
    @RequestMapping(method = RequestMethod.GET)
    public List<Ticket> getTicketsByEmail(@RequestParam String email) {
        return this.ticketService.getTicketsByMasterEmail(email);
    }

    @RequestMapping(method = RequestMethod.PUT, params = {"ticketId", "updatedStatus"})
    public void updateStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus) {
        try {
            this.masterService.updateStatus(ticketId, updatedStatus);
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't update the status, see: " + e);
        }
    }

}
