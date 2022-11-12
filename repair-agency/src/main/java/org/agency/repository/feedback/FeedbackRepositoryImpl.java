package org.agency.repository.feedback;

import org.agency.entity.Feedback;
import org.agency.entity.Ticket;

import java.sql.Connection;
import java.util.List;

public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final Connection connection;

    public FeedbackRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Ticket findById(Long id) {
        return null;
    }

    @Override
    public void create(Feedback t) {
    }

    @Override
    public void update(Feedback t) {
    }

    @Override
    public void delete(Long id) {
    }

}
