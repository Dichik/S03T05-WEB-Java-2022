package org.agency.service.ticket;

import org.agency.entity.Master;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.MasterLackOfPermissionException;
import org.agency.exception.TicketNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.ticket.TicketRepository;
import org.agency.service.session.CurrentSession;

import java.math.BigDecimal;
import java.util.Objects;

public class TicketService {

    private final TicketRepository ticketRepository;
    private final MasterRepository masterRepository;

    public TicketService(TicketRepository ticketRepository, MasterRepository masterRepository) {
        this.ticketRepository = ticketRepository;
        this.masterRepository = masterRepository;
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.create(ticket);
    }

    // FIXME check if operation made by master role
    public void updateStatus(Long ticketId, String updatedStatusName) throws TicketNotFoundException, UnvalidStatusUpdateException {
        // FIXME we update status from two different roles, so we should check when an action is valid
        // FIXME check if the current ticket is assigned to this master_id
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with " + ticketId + " was not found.");
        }

        Master master = CurrentSession.getSession().getMaster();
        if (!Objects.equals(ticket.getMasterId(), master.getId())) {
            throw new MasterLackOfPermissionException("Oops, it seems like you are trying to update not your ticket...");
        }

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
        if (this.validateStatusChange(currentStatus, updatedStatus)) {
            assert updatedStatus != null;
            String message = String.format("Oops, status update from %s to %s is not valid for current user",
                    currentStatus.getStatus(), updatedStatus.getStatus());
            throw new UnvalidStatusUpdateException(message);
        }
        ticket.setStatus(updatedStatus);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

    private boolean validateStatusChange(TicketStatus oldStatus, TicketStatus newStatus) {
        // TODO fix this method
        return oldStatus.equals(newStatus) || (oldStatus.equals(TicketStatus.NEW) && newStatus.equals(TicketStatus.IN_PROGRESS));
    }

    public Ticket updatePrice(Long ticketId, BigDecimal price) throws TicketNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with " + ticketId + " was not found.");
        }
        ticket.setPrice(price);
        this.ticketRepository.update(ticket.getId(), ticket); // FIXME
        return ticket;
    }

    public void assignMaster(Long ticketId, Long masterId) throws TicketNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with " + ticketId + " was not found.");
        }

        // FIXME would be really great to know here if master_id is correct
        ticket.setMasterId(masterId);
        this.ticketRepository.update(ticket.getId(), ticket); // FIXME
    }

    public boolean ticketExistsById(Long ticketId) {
        return !(this.ticketRepository.findById(ticketId) == null);
    }

}
