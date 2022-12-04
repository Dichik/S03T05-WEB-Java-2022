package org.agency.controller;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Ticket;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.service.auth.AuthService;
import org.agency.service.master.MasterService;
import org.agency.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MasterController {
    private static final Logger logger = LogManager.getLogger(MasterController.class);

    private final TicketService ticketService;
    private final MasterService masterService;
    private final AuthService authService;

    public MasterController(ServiceDelegator serviceDelegator) {
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.masterService = (MasterService) serviceDelegator.getByClass(MasterService.class);
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    public void updateStatus(Long ticketId, String updatedStatus) {
        try {
            this.masterService.updateStatus(ticketId, updatedStatus);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't update the status, see: " + e);
        } catch (UnvalidStatusUpdateException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        this.authService.logout();
    }

    public List<Ticket> getTicketsByEmail(String email) {
        return this.ticketService.getTicketsByMasterEmail(email);
    }

}
