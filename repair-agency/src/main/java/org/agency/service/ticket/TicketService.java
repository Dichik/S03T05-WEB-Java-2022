package org.agency.service.ticket;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.ticket.TicketRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.agency.service.session.Session;

import java.math.BigDecimal;
import java.util.List;

public class TicketService implements BaseService {

    private final TicketRepository ticketRepository;
    private final MasterRepository masterRepository;

    public TicketService(RepositoryDelegator repositoryDelegator) {
        this.ticketRepository = (TicketRepository) repositoryDelegator.getByClass(TicketRepository.class);
        this.masterRepository = (MasterRepository) repositoryDelegator.getByClass(MasterRepository.class);
    }

    public void createTicket(Ticket ticket) {
        this.ticketRepository.create(ticket);
    }

    public List<Ticket> getTicketsByUserEmail(String email) {
        return this.ticketRepository.getByUserEmail(email);
    }

    public List<Ticket> getTicketsByMasterEmail(String email) {
        return this.ticketRepository.getByMasterEmail(email);
    }

    // FIXME check if operation made by master role
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
        if (this.validateStatusChange(currentStatus, updatedStatus)) {
            assert updatedStatus != null;
            String message = String.format("Oops, status update from %s to %s is not valid for current user",
                    currentStatus.getName(), updatedStatus.getName());
            throw new UnvalidStatusUpdateException(message);
        }
        ticket.setStatus(updatedStatus);
        this.ticketRepository.update(ticket.getId(), ticket);
    }

    private boolean validateStatusChange(TicketStatus oldStatus, TicketStatus newStatus) {
        // TODO fix this method
        return oldStatus.equals(newStatus) || (oldStatus.equals(TicketStatus.NEW) && newStatus.equals(TicketStatus.IN_PROGRESS));
    }

    public Ticket updatePrice(Long ticketId, BigDecimal price) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket with " + ticketId + " was not found.");
        }
        ticket.setPrice(price);
        this.ticketRepository.update(ticket.getId(), ticket); // FIXME
        return ticket;
    }

    public void assignMaster(Long ticketId, String masterId) throws EntityNotFoundException {
        Ticket ticket = this.ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket with " + ticketId + " was not found.");
        }

        // FIXME would be really great to know here if master_id is correct
        ticket.setMasterEmail(masterId);
        this.ticketRepository.update(ticket.getId(), ticket); // FIXME
    }

    public boolean ticketExistsById(Long ticketId) {
        return !(this.ticketRepository.findById(ticketId) == null);
    }

}
