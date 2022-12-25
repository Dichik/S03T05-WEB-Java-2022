package org.agency.repository.feedback;

import org.agency.entity.Feedback;
import org.agency.repository.DaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class FeedbackRepository extends DaoImpl<Feedback> {

    @Autowired
    public FeedbackRepository(Connection connection) {
        super(connection, "feedbacks");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " + this.tableName + "(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "text VARCHAR(255) NOT NULL, " +
                "ticketId BIGINT NOT NULL, " +
                "userEmail VARCHAR(255) NOT NULL)";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (text, ticketId, userEmail) " +
                "VALUES (?, ?, ?)";
    }

    @Override
    public Feedback buildItem(ResultSet rs) throws SQLException {
        return Feedback.builder()
                .text(rs.getString("text"))
                .id(rs.getLong("id"))
                .ticketId(rs.getLong("ticketId"))
                .userEmail(rs.getString("userEmail"))
                .build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Feedback item, boolean setId) throws SQLException {
        if (item.getText() != null) {
            ps.setString(1, item.getText());
        } else ps.setNull(1, Types.NULL);

        if (item.getTicketId() != null) {
            ps.setLong(2, item.getTicketId());
        } else ps.setNull(2, Types.NULL);

        if (item.getUserEmail() != null) {
            ps.setString(3, item.getUserEmail());
        } else ps.setNull(3, Types.NULL);

        if (setId) {
            ps.setLong(4, item.getId());
        }

    }

    @Override
    public String getUpdateSQLQuery() {
        return null;
    }

}
