package org.agency.service.user;

import org.agency.entity.Ticket;
import org.agency.repository.TicketRepository;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.createTicket(ticket);
    }

}
