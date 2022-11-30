package org.agency.delegator;

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

public class RepositoryDelegator implements Delegator<BaseRepository<?>> {

    private final Map<Class<?>, BaseRepository<?>> repositories;

    public RepositoryDelegator(Connection connection) {
        this.repositories = new HashMap<>(){{
            put(FeedbackRepository.class, new FeedbackRepository(connection));
            put(ManagerRepository.class, new ManagerRepository(connection));
            put(MasterRepository.class, new MasterRepository(connection));
            put(TicketRepository.class, new TicketRepository(connection));
            put(UserRepository.class, new UserRepository(connection));
        }};
    }

    @Override
    public boolean existsByClass(Class<?> clazz) {
        return this.repositories.containsKey(clazz);
    }

    @Override
    public BaseRepository<?> getByClass(Class<?> clazz) {
        return this.repositories.getOrDefault(clazz, null);
    }

}
