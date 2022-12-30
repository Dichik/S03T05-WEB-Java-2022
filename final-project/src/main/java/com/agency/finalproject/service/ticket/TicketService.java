package com.agency.finalproject.service.ticket;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        return this.ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUserEmail(String email) {
        return this.ticketRepository.findByUserEmail(email);
    }

    public List<Ticket> getTicketsByMasterEmail(String email) {
        return this.ticketRepository.findByMasterEmail(email);
    }

    public Ticket updatePrice(Long ticketId, BigDecimal price) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));
        ticket.setPrice(price);
        return this.ticketRepository.save(ticket);
    }

    public Ticket assignMaster(Long ticketId, String masterEmail) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));
        ticket.setMasterEmail(masterEmail);
        return this.ticketRepository.save(ticket);
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
        return this.ticketRepository.findByMasterEmail(masterEmail);
    }

    public List<Ticket> getFilteredByStatus(TicketStatus status) {
        return this.ticketRepository.findByStatus(status);
    }

    public Optional<Ticket> getById(Long ticketId) {
        return this.ticketRepository.findById(ticketId);
    }

    public Ticket updateTicket(Ticket ticket) {
        return this.ticketRepository.save(ticket);
    }

    public Ticket update(Long id, TicketDto ticket) {
        return this.ticketRepository.save()
    }
}
