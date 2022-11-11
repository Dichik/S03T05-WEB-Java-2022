package org.agency.service.ticket;

import org.agency.entity.Ticket;
import org.agency.repository.ticket.TicketRepositoryImpl;

public class TicketService {

    private final TicketRepositoryImpl ticketRepository;

    public TicketService(TicketRepositoryImpl ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.createTicket(ticket);
    }

}
