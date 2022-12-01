package org.agency.service.master;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.repository.ticket.TicketRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.agency.service.session.Session;

public class MasterService implements BaseService {

    private final TicketRepository ticketRepository;

    public MasterService(RepositoryDelegator repositoryDelegator) {
        this.ticketRepository = (TicketRepository) repositoryDelegator.getByClass(TicketRepository.class);
    }

    public void updateStatus(Long ticketId, String updatedStatusName) throws EntityNotFoundException, UnvalidStatusUpdateException {
        // FIXME we update status from two different roles, so we should check when an action is valid
        // FIXME check if the current ticket is assigned to this master_id
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket with " + ticketId + " was not found.");
        }

        Session session = CurrentSession.getSession();
//        if (!Objects.equals(ticket.getMasterId(), master.getId())) {
//            throw new MasterLackOfPermissionException("Oops, it seems like you are trying to update not your ticket...");
//        }

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus updatedStatus = TicketStatus.getTicketStatusByName(updatedStatusName);
//        if (this.validateStatusChange(currentStatus, updatedStatus)) {
//            assert updatedStatus != null;
//            String message = String.format("Oops, status update from %s to %s is not valid for current user",
//                    currentStatus.getName(), updatedStatus.getName());
//            throw new UnvalidStatusUpdateException(message);
//        }
        ticket.setStatus(updatedStatus);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

}
