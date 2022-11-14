package org.agency.service.ticket;

import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.TicketNotFoundException;
import org.agency.repository.ticket.TicketRepository;

import java.math.BigDecimal;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.create(ticket);
    }

    public void updateStatus(Long ticketId, String updatedStatusName) throws TicketNotFoundException {
        // FIXME we update status from two different roles, so we should check when an action is valid
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException("Ticket with " + ticketId + " was not found.");
        }
        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
        if (this.validateStatusChange(currentStatus, updatedStatus)) {
            return; // TODO throw corresponded custom exception
        }
        ticket.setStatus(updatedStatus);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

    private boolean validateStatusChange(TicketStatus oldStatus, TicketStatus newStatus) {

        // FIXME get currentSession and check who is currently authorised

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
