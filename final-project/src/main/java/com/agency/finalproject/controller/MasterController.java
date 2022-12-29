package com.agency.finalproject.controller;

import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.service.master.MasterService;
import com.agency.finalproject.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/masters")
public class MasterController {
    private static final Logger logger = LogManager.getLogger(MasterController.class);

    private final TicketService ticketService;
    private final MasterService masterService;

    @Autowired
    public MasterController(TicketService ticketService, MasterService masterService) {
        this.ticketService = ticketService;
        this.masterService = masterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Ticket>> getTicketsByEmail(@RequestParam String email) {
        List<Ticket> tickets = this.ticketService.getTicketsByMasterEmail(email);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, params = {"ticketId", "updatedStatus"})
    public ResponseEntity<?> updateStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus) {
        try {
            this.masterService.updateStatus(ticketId, updatedStatus);
            return new ResponseEntity<>("Master was successfully updated!", HttpStatus.OK);
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            String message = "Couldn't update the status";
            logger.error(message + ", see: " + e);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

}
