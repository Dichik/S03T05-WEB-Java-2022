package org.agency.service.ticket;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.Ticket;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.ticket.TicketRepository;
import org.agency.service.BaseService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TicketService implements BaseService {

    private final TicketRepository ticketRepository;

    public TicketService(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.ticketRepository = (TicketRepository) repositoryDelegator.getByClass(TicketRepository.class);
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.create(ticket);
    }

    public List<Ticket> getTicketsByUserEmail(String email) {
        return this.ticketRepository.getByUserEmail(email);
    }

    public List<Ticket> getTicketsByMasterEmail(String email) {
        return this.ticketRepository.getByMasterEmail(email);
    }

    public void updatePrice(Long ticketId, BigDecimal price) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));
        ticket.setPrice(price);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

    public void assignMaster(Long ticketId, String masterEmail) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));
        ticket.setMasterEmail(masterEmail);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

    public boolean ticketExistsById(Long ticketId) {
        return this.ticketRepository.findById(ticketId).isPresent();
    }

    public List<Ticket> getAll() {
        return this.ticketRepository.findAll();
    }

    public List<Ticket> getSortedByDate() {
        List<Ticket> tickets = this.ticketRepository.findAll();
        tickets.sort(Comparator.comparing(Ticket::getCreatedAt));
        return tickets;
    }

    public List<Ticket> getSortedByPrice() {
        List<Ticket> tickets = this.ticketRepository.findAll();
        tickets.sort(Comparator.comparing(Ticket::getPrice));
        return tickets;
    }

    public List<Ticket> getSortedByStatus() {
        List<Ticket> tickets = this.ticketRepository.findAll();
        tickets.sort(Comparator.comparing(Ticket::getStatus));
        return tickets;
    }

    public List<Ticket> getFilteredByMaster(String masterEmail) {
        return this.ticketRepository.getByMasterEmail(masterEmail);
    }

    public List<Ticket> getFilteredByStatus(String status) {
        return this.ticketRepository.getByStatus(status);
    }

    public Optional<Ticket> getById(Long ticketId) {
        return this.ticketRepository.findById(ticketId);
    }
}
