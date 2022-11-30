package org.agency.repository.feedback;

import org.agency.entity.Feedback;
import org.agency.repository.BaseRepositoryImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackRepository extends BaseRepositoryImpl<Feedback> {

    public FeedbackRepository(Connection connection) {
        super(connection, "feedbacks");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "";
    }

    @Override
    public Feedback buildItem(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Feedback item) throws SQLException {

    }

    @Override
    public void create(Feedback feedback) {

    }
}
