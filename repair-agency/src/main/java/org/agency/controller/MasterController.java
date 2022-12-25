package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.service.master.MasterService;
import org.agency.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/masters")
public class MasterController {
    private static final Logger logger = LogManager.getLogger(MasterController.class);

    private final TicketService ticketService;
    private final MasterService masterService;

    @Autowired
    public MasterController(TicketService ticketService, MasterService masterService) {
        this.ticketService = ticketService;
        this.masterService = masterService;
    }

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
