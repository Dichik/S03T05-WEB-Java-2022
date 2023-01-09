package com.agency.finalproject.service.ticket;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.ticket.dto.TicketDto;
import com.agency.finalproject.exception.InvalidStatusChangeException;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.RoleLackOfPermissionException;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.repository.user.UserRepository;
import com.agency.finalproject.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Ticket ticket, String userEmail) {
        ticket.setStatus(TicketStatus.NEW);
        ticket.setUserEmail(userEmail);
        ticket.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return this.ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUserEmail(String email) {
        return this.ticketRepository.findByUserEmail(email);
    }

    public List<Ticket> getTicketsByMasterEmail(String email) {
        return this.ticketRepository.findByMasterEmail(email);
    }

    public Ticket assignMaster(Long ticketId, String masterEmail) throws ItemWasNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ItemWasNotFoundException("Ticket with " + ticketId + " was not found."));
        // maybe master will be created in the future
//        if (!this.userRepository.existsByEmail(masterEmail)) {
//            throw new ItemWasNotFoundException("Master with email=" + masterEmail + " was not found.");
//        }
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

    public List<Ticket> getFilteredByStatus(String status) throws EntityNotFoundException {
        TicketStatus ticketStatus = TicketStatus.getTicketStatusByName(status);
        if (ticketStatus == null) {
            throw new EntityNotFoundException("You are trying to filter by wrong status.");
        }
        return this.ticketRepository.findByStatus(ticketStatus);
    }

    public Ticket updateTicketPrice(Long id, TicketDto ticketDto) throws ItemWasNotFoundException {
        Ticket ticket = this.ticketRepository.findById(id)
                .orElseThrow(() -> new ItemWasNotFoundException("Ticket was not found"));
        ticket.setPrice(ticketDto.getPrice());
        return this.ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Long ticketId, String updatedStatusName, UserDetailsImpl userDetails) throws EntityNotFoundException, UnvalidStatusUpdateException, InvalidStatusChangeException, ItemWasNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
        if (updatedStatus == null) {
            throw new ItemWasNotFoundException("Updated status is not valid, please specify the correct one.");
        }

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_MASTER") && !roles.contains("ROLE_MANAGER")
                && !Objects.equals(ticket.getMasterEmail(), userDetails.getEmail())) {
            throw new RoleLackOfPermissionException("It seems like you are trying to update not your ticket...");
        }

        if (!this.validateStatusChange(currentStatus, updatedStatus, roles)) {
            throw new InvalidStatusChangeException(String.format("Invalid status change from %s to %s for current user.",
                    currentStatus.getName(), updatedStatus.getName()));
        }

        ticket.setStatus(updatedStatus);
        return this.ticketRepository.save(ticket);
    }

    private boolean validateStatusChange(TicketStatus current, TicketStatus updated, Set<String> roles) throws InvalidStatusChangeException {
        for (String role : roles) {
            if ("ROLE_MANAGER".equals(role) && validateStatusChangeForManager(current, updated)) {
                return true;
            } else if ("ROLE_MASTER".equals(role) && validateStatusChangeForMaster(current, updated)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateStatusChangeForManager(TicketStatus currentStatus, TicketStatus updatedStatus) {
        if (currentStatus == TicketStatus.DONE && updatedStatus == TicketStatus.WAITING_FOR_PAYMENT) {
            return true;
        } else if (currentStatus == TicketStatus.DONE && updatedStatus == TicketStatus.PAID) {
            return true;
        }
        return currentStatus == TicketStatus.CANCELED;
    }

    private boolean validateStatusChangeForMaster(TicketStatus currentStatus, TicketStatus updatedStatus) {
        if (currentStatus == TicketStatus.NEW && updatedStatus == TicketStatus.IN_PROGRESS) {
            return true;
        }
        return currentStatus == TicketStatus.IN_PROGRESS && updatedStatus == TicketStatus.DONE;
    }

}
