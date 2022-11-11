package org.agency.service.user;

import org.agency.entity.Ticket;

import java.sql.Connection;
import java.util.List;

public class UserService {

    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    // TODO add annotatio
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
