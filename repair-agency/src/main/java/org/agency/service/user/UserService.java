package org.agency.service.user;

import org.agency.entity.Ticket;

import java.util.List;

public class UserService {

    public UserService() {

    }

    // TODO add annotation
    public void createTicket(Ticket ticket) {
        // TODO need to be implemented
    }

    public List<Ticket> getActiveTickets() {
        // TODO implement this method
        return null;
    }

    public List<Ticket> getDoneTickets() {
        // TODO implement this method
        return null;
    }

    public boolean leaveFeedback(Long ticketId) {

        // TODO should be mapped from dto_id to entity_id

        return false;
    }

}
