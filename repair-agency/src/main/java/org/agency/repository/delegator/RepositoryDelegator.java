package org.agency.repository.delegator;

import org.agency.entity.*;
import org.agency.repository.BaseRepository;
import org.agency.repository.feedback.FeedbackRepository;
import org.agency.repository.manager.ManagerRepository;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.ticket.TicketRepository;
import org.agency.repository.user.UserRepository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class RepositoryDelegator {

    private final Map<Class<?>, BaseRepository<?>> repositories;

    public RepositoryDelegator(Connection connection) {
        this.repositories = new HashMap<>(){{
            put(Feedback.class, new FeedbackRepository(connection));
            put(Manager.class, new ManagerRepository(connection));
            put(Master.class, new MasterRepository(connection));
            put(Ticket.class, new TicketRepository(connection));
            put(User.class, new UserRepository(connection));
        }};
    }

    public BaseRepository<?> getByClass(Class<?> clazz) {
        return this.repositories.getOrDefault(clazz, null);
    }

}
