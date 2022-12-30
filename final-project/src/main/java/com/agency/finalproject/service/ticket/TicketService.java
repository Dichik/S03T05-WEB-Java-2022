package com.agency.finalproject.service.ticket;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.ticket.dto.TicketDto;
import com.agency.finalproject.exception.RoleLackOfPermissionException;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.security.service.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, ModelMapper modelMapper) {
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

    public Ticket assignMaster(Long ticketId, String masterEmail) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));
        ticket.setMasterEmail(masterEmail);
        return this.ticketRepository.save(ticket);
    }

    public List<Ticket> getAll() {
        return this.ticketRepository.findAll();
    }

    public List<Ticket> getSortedByDate() {
        return this.getTicketsByComparator(Comparator.comparing(Ticket::getCreatedAt));
    }

    public List<Ticket> getSortedByPrice() {
        return this.getTicketsByComparator(Comparator.comparing(Ticket::getPrice));
    }

    public List<Ticket> getSortedByStatus() {
        return this.getTicketsByComparator(Comparator.comparing(Ticket::getStatus));
    }

    private List<Ticket> getTicketsByComparator(Comparator<Ticket> comparator) {
        List<Ticket> tickets = this.ticketRepository.findAll();
        tickets.sort(comparator);
        return tickets;
    }

    public List<Ticket> getFilteredByMasterEmail(String masterEmail) {
        return this.ticketRepository.findByMasterEmail(masterEmail);
    }

    public List<Ticket> getFilteredByStatus(String status) {
        TicketStatus ticketStatus = TicketStatus.valueOf(status); // FIXME
        return this.ticketRepository.findByStatus(ticketStatus);
    }

    public Optional<Ticket> getById(Long ticketId) {
        return this.ticketRepository.findById(ticketId);
    }

    public Ticket updateTicketPrice(Long id, TicketDto ticketDto) {
        Ticket ticket = this.ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket was not found"));
        ticket.setPrice(ticketDto.getPrice());
        return this.ticketRepository.save(ticket);
    }

    public void updateStatus(Long ticketId, String updatedStatusName, UserDetailsImpl userDetails) throws EntityNotFoundException, UnvalidStatusUpdateException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));

        checkPermissions(ticket, userDetails);

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
        if (!this.validateStatusChange(currentStatus, updatedStatus)) {
            assert updatedStatus != null;
            String message = String.format("Oops, status update from [%s] to [%s] is not valid for current user",
                    currentStatus.getName(), updatedStatus.getName());
            throw new UnvalidStatusUpdateException(message);
        }

        ticket.setStatus(updatedStatus);
        this.ticketRepository.save(ticket);
    }

    private void checkPermissions(Ticket ticket, UserDetailsImpl userDetails) {
        if (!Objects.equals(ticket.getMasterEmail(), userDetails.getEmail())) {
            throw new RoleLackOfPermissionException("Oops, it seems like you are trying to update not your ticket...");
        }
    }

    private boolean validateStatusChange(TicketStatus currentStatus, TicketStatus updatedStatus) {
        if (currentStatus == TicketStatus.DONE && updatedStatus == TicketStatus.WAITING_FOR_PAYMENT) {
            return true;
        } else if (currentStatus == TicketStatus.DONE && updatedStatus == TicketStatus.PAID) {
            return true;
        }
        return currentStatus == TicketStatus.CANCELED;
    }

}
