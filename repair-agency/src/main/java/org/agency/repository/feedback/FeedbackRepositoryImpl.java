package org.agency.repository.feedback;

import org.agency.entity.Feedback;

import java.sql.Connection;
import java.util.List;

public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final Connection connection;

    public FeedbackRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Feedback> findAll() {
        return null;
    }

    @Override
    public Feedback findById(Long id) {
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
