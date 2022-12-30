package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/assign_master", method = RequestMethod.POST, params = {"ticketId", "masterEmail"})
    public ResponseEntity<?> assignMasterToTicket(@RequestParam Long ticketId, @RequestParam String masterEmail) {
        try {
            Ticket ticket = this.ticketService.assignMaster(ticketId, masterEmail);
            String message = String.format("Successfully assigned master [email=%s] to ticket [id=%d]", masterEmail, ticketId);

            Map<String, Object> body = new LinkedHashMap<>(){{
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
    public ResponseEntity<?> setTicketPrice(@PathVariable Long id, @RequestBody Map<String, Object> ticket) {
        try {
            return new ResponseEntity<>(this.ticketService.update(id, null), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't set ticket price, see: " + e);
            return new ResponseEntity<>(new MessageResponse("Couldn't find ticket"), HttpStatus.NOT_FOUND);
        }
    }

    public List<Ticket> getSortedByDate() {
        return this.ticketService.getSortedByDate();
    }

    public List<Ticket> getSortedByStatus() {
        return this.ticketService.getSortedByStatus();
    }

    public List<Ticket> getSortedByPrice() {
        return this.ticketService.getSortedByPrice();
    }

    public List<Ticket> getFilteredByMaster(String masterEmail) {
        return this.ticketService.getFilteredByMaster(masterEmail);
    }

    public List<Ticket> getFilterByStatus(String status) {
        return this.ticketService.getFilteredByStatus(TicketStatus.valueOf(status));
    }

}
