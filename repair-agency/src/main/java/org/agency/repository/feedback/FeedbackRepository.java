package org.agency.repository.feedback;

import org.agency.entity.Feedback;
import org.agency.repository.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackRepository extends DaoImpl<Feedback> {

    public FeedbackRepository(Connection connection) {
        super(connection, "feedbacks"); // FIXME take name from the correct place

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "";
    }

    @Override
    public String getInsertSQLQuery() {
        return null;
    }

    @Override
    public Feedback buildItem(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Feedback item, boolean setId) throws SQLException {

    }

    @Override
    public String getUpdateSQLQuery() {
        return null;
    }

}
