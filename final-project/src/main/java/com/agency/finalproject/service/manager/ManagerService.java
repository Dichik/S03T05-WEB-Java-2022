package com.agency.finalproject.service.manager;

import com.agency.finalproject.entity.Ticket;
import com.agency.finalproject.entity.TicketStatus;
import com.agency.finalproject.exception.MasterLackOfPermissionException;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.repository.ticket.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
public class ManagerService {
    private final Logger logger = LogManager.getLogger(ManagerService.class);

    private final TicketRepository ticketRepository;

    @Autowired
    public ManagerService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void updateStatus(Long ticketId, String updatedStatusName) throws EntityNotFoundException, UnvalidStatusUpdateException {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " was not found."));

        checkPermissions(ticket);

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
        logger.info(String.format("Ticket with id=[%d] was updated from [%s] to [%s]",
                ticket.getId(), currentStatus.getName(), Objects.requireNonNull(updatedStatus).getName()));
    }

    private void checkPermissions(Ticket ticket) {
        if (!Objects.equals(ticket.getMasterEmail(), null)) {
            throw new MasterLackOfPermissionException("Oops, it seems like you are trying to update not your ticket...");
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
