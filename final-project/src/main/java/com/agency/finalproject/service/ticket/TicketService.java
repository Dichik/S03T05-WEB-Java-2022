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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Ticket> getFilteredByStatus(String status) throws EntityNotFoundException {
        TicketStatus ticketStatus = TicketStatus.getTicketStatusByName(status);
        if (ticketStatus == null) {
            throw new EntityNotFoundException("You are trying to filter by wrong status.");
        }
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

    public Ticket updateStatus(Long ticketId, String updatedStatusName, UserDetailsImpl userDetails) throws EntityNotFoundException, UnvalidStatusUpdateException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
        if (updatedStatus == null) {
            throw new EntityNotFoundException("Updated status is not valid, please specify the correct one.");
        }

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_MASTER") && !roles.contains("ROLE_MANAGER")
                && !Objects.equals(ticket.getMasterEmail(), userDetails.getEmail())) {
            throw new RoleLackOfPermissionException("Oops, it seems like you are trying to update not your ticket...");
        }

        if (!this.validateStatusChange(currentStatus, updatedStatus, roles)) {
            String message = String.format("Oops, status update from [%s] to [%s] is not valid for current user",
                    currentStatus.getName(), updatedStatus.getName());
            throw new UnvalidStatusUpdateException(message);
        }

        ticket.setStatus(updatedStatus);
        return this.ticketRepository.save(ticket);
    }

    private boolean validateStatusChange(TicketStatus current, TicketStatus updated, Set<String> roles) {
        for (String role : roles) {
            if ("ROLE_MANAGER".equals(role) && validateStatusChangeForManager(current, updated)) {
                return true;
            } else if ("ROLE_MASTER".equals(role) && validateStatusChangeForMaster(current, updated)) {
                return true;
            } else {
                throw new RoleLackOfPermissionException("Role doesn't have enough permissions.");
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
