package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.dto.TicketDto;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private static final Logger logger = LogManager.getLogger(TicketController.class);

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Autowired
    public TicketController(TicketService ticketService, ModelMapper modelMapper) {
        this.ticketService = ticketService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getTickets() {
        List<Ticket> tickets = this.ticketService.getAll();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>("No tickets were found.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<?> getTicketsByUserEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getEmail();
        List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(email);
        if (tickets.isEmpty()) {
            logger.info("No tickets were found for email=" + email);
            return new ResponseEntity<>(new MessageResponse("No tickets were found."), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MASTER')")
    @RequestMapping(value = "/master", method = RequestMethod.GET)
    public ResponseEntity<?> getTicketsByMasterEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String masterEmail = userDetails.getEmail();
        List<Ticket> tickets = this.ticketService.getTicketsByMasterEmail(masterEmail);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>("No tickets were found.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody TicketDto ticketDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Ticket ticket = this.modelMapper.map(ticketDto, Ticket.class);
        ticket.setUserEmail(userDetails.getEmail());
        return new ResponseEntity<>(this.ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(value = "/assign_master", method = RequestMethod.POST, params = {"ticketId", "masterEmail"})
    public ResponseEntity<?> assignMasterToTicket(@RequestParam Long ticketId, @RequestParam String masterEmail) {
        try {
            Ticket ticket = this.ticketService.assignMaster(ticketId, masterEmail);
            String message = String.format("Successfully assigned master [email=%s] to ticket [id=%d]", masterEmail, ticketId);

            Map<String, Object> body = new LinkedHashMap<>() {{
                put("data", ticket);
                put("message", message);
            }};
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't assign master to the ticket, see: " + e);
            return new ResponseEntity<>("Couldn't assign master, please try again.", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(value = "/{id:\\d+}/price", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateTicketPrice(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        try {
            return new ResponseEntity<>(this.ticketService.updateTicketPrice(id, ticketDto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
            return new ResponseEntity<>(new MessageResponse("Couldn't find ticket"), HttpStatus.NOT_FOUND);
        }
    }

    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_MASTER"})
    @RequestMapping(method = RequestMethod.PUT, params = {"ticketId", "updatedStatus"})
    public ResponseEntity<?> setStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Ticket ticket = this.ticketService.updateStatus(ticketId, updatedStatus, userDetails);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
            return new ResponseEntity<>("Couldn't update status.", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.GET, params = {"filter"})
    public ResponseEntity<?> getFilteredBy(@RequestParam String filter) {
        List<Ticket> tickets;
        switch (filter) {
            case "masterEmail" -> {
                tickets = this.ticketService.getFilteredByMasterEmail(filter);
            }
            case "status" -> {
                tickets = this.ticketService.getFilteredByStatus(filter);
            }
            default -> {
                return new ResponseEntity<>(new MessageResponse("Can't get tickets filtered by " + filter), HttpStatus.NOT_IMPLEMENTED);
            }
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.GET, params = {"sortBy"})
    public ResponseEntity<?> getSortedBy(@RequestParam String sortBy) {
        List<Ticket> tickets;
        switch (sortBy) {
            case "date" -> {
                tickets = this.ticketService.getSortedByDate();
            }
            case "status" -> {
                tickets = this.ticketService.getSortedByStatus();
            }
            case "price" -> {
                tickets = this.ticketService.getSortedByPrice();
            }
            default -> {
                return new ResponseEntity<>(new MessageResponse("Can't get tickets sorted by " + sortBy), HttpStatus.NOT_IMPLEMENTED);
            }
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

}
