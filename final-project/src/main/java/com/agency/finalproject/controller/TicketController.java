package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.dto.TicketDto;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// TODO refactor and fix issues

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private static final Logger logger = LogManager.getLogger(TicketController.class);

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getTickets() {
        List<Ticket> tickets = this.ticketService.getAll();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>("No tickets were found.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET, params = {"byEmail"})
    public ResponseEntity<?> getTicketsByUserEmail(@RequestParam Optional<Boolean> byEmail, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (byEmail.isEmpty() || !byEmail.get()) {
            return getTickets();
        }
        String email = userDetails.getEmail();
        List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(email);
        if (tickets.isEmpty()) {
            logger.info("No tickets were found for email=" + email);
            return new ResponseEntity<>(new MessageResponse("No tickets were found."), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Secured("ROLE_MASTER")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getTicketsByMasterEmail(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String masterEmail = userDetails.getEmail();
        List<Ticket> tickets = this.ticketService.getTicketsByMasterEmail(masterEmail);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>("No tickets were found.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(this.ticketService.createTicket(ticket), HttpStatus.CREATED);
    }

    @Secured("ROLE_MANAGER")
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

    // TODO finish implementation...
    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.PATCH)
    public ResponseEntity<?> setTicketPrice(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        try {
            return new ResponseEntity<>(this.ticketService.update(id, ticketDto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
            return new ResponseEntity<>(new MessageResponse("Couldn't find ticket"), HttpStatus.NOT_FOUND);
        }
    }

    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_MASTER"})
    @RequestMapping(method = RequestMethod.PUT, params = {"ticketId", "updatedStatus"})
    public void setStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus,
                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            this.ticketService.updateStatus(ticketId, updatedStatus, userDetails);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

    @Secured("ROLE_MANAGER")
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

    @Secured("ROLE_MANAGER")
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
