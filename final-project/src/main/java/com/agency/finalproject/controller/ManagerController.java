package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.service.manager.ManagerService;
import com.agency.finalproject.service.ticket.TicketService;
import com.agency.finalproject.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {
    private static final Logger logger = LogManager.getLogger(ManagerController.class);

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }



    // FIXME
    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "updatedStatus"})
    public void setStatus(@RequestParam Long ticketId, @RequestParam String updatedStatus) {
        try {
            this.managerService.updateStatus(ticketId, updatedStatus);
            logger.info(String.format("Ticket status with id=%d was updated to status=%s", ticketId, updatedStatus));
        } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
            logger.error("Couldn't set status " + updatedStatus + ", see: " + e);
        }
    }

}
