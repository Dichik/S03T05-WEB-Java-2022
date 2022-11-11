package org.agency.controller;

import org.agency.entity.Ticket;
import org.agency.exception.TicketNotFoundException;
import org.agency.service.master.MasterService;
import org.agency.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MasterController {
    private static final Logger logger = LogManager.getLogger(MasterController.class);

    private final MasterService masterService;
    private final TicketService ticketService;

    public MasterController(MasterService masterService, TicketService ticketService) {
        this.masterService = masterService;
        this.ticketService = ticketService;
    }

    public void updateStatus(Long ticketId, String updatedStatus) {
        // FIXME check if the current ticket is assigned to this master_id
        try {
            this.ticketService.updateStatus(ticketId, updatedStatus);
        } catch (TicketNotFoundException e) {
            logger.error("Couldn't update the status, see: ");
        }

    }

}
