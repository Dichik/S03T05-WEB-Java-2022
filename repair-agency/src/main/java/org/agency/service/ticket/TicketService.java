package org.agency.service.ticket;

import org.agency.entity.Ticket;
import org.agency.repository.ticket.TicketRepository;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.create(ticket);
    }

    public void updateStatus(Long ticketId, String updatedStatus) {
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            return; // TODO throw corresponded custom exception
        }
        String currentStatus = ticket.getStatus();
        if (this.validateStatusChange(currentStatus, updatedStatus)) {
            return; // TODO throw corresponded custom exception
        }
        ticket.setStatus(updatedStatus);
        this.ticketRepository.update(ticket);
    }

    private boolean validateStatusChange(String oldStatus, String newStatus) {
        // TODO fix this method
        return oldStatus.equals(newStatus) || (oldStatus.equals("new") && newStatus.equals("in_progress"));
    }

}
