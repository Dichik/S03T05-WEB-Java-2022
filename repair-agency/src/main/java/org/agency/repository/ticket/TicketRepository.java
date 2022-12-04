package org.agency.repository.ticket;

import lombok.Value;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.SQLOperationException;
import org.agency.repository.DaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends DaoImpl<Ticket> {
    private static final Logger logger = LogManager.getLogger(TicketRepository.class);

    public TicketRepository(Connection connection) {
        super(connection, "tickets");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " + this.tableName + "(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "title VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255) NOT NULL, " +
                "userEmail VARCHAR(255) NOT NULL, " +
                "status VARCHAR(255), " +
                "masterEmail VARCHAR(255), " +
                "price DECIMAL DEFAULT NULL, " +
                "createdAt TIMESTAMP)";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (title, description, userEmail, status, masterEmail, price, createdAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public Ticket buildItem(ResultSet rs) throws SQLException {
        return new Ticket.TicketBuilder(rs.getString("title"), rs.getString("description"), rs.getString("userEmail"))
                .setId(rs.getLong("id"))
                .setStatus(TicketStatus.getTicketStatusByName(rs.getString("status")))
                .setMasterId(rs.getString("masterEmail"))
                .setPrice(rs.getBigDecimal("price"))
                .setCreatedAt(rs.getTimestamp("createdAt"))
                .build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Ticket item, boolean setId) throws SQLException {
        ps.setString(1, item.getTitle());
        ps.setString(2, item.getDescription());
        ps.setString(3, item.getUserEmail());
        ps.setString(4, item.getStatus().getName());

        if (item.getMasterEmail() != null) {
            ps.setString(5, item.getMasterEmail());
        } else ps.setNull(5, Types.NULL);

        ps.setBigDecimal(6, item.getPrice());
        ps.setTimestamp(7, item.getCreatedAt());

        if (setId) {
            ps.setLong(8, item.getId());
        }
    }

    @Override
    public String getUpdateSQLQuery() {
        return "UPDATE " + this.tableName +
                " SET title=?, description=?, userEmail=?, status=?, masterEmail=?, price=?, createdAt=?" +
                " WHERE id=?";
    }

    public List<Ticket> getByUserEmail(String email) {
        String sql = "SELECT * from " + this.tableName + " WHERE userEmail='" + email + "'";
        return this.getBy(sql);
    }

    public List<Ticket> getByMasterEmail(String email) {
        String sql = "SELECT * from " + this.tableName + " WHERE masterEmail='" + email + "'";
        return this.getBy(sql);
    }

    public List<Ticket> getByStatus(String status) {
        String sql = "SELECT * from " + this.tableName + " WHERE status='" + status + "'";
        return this.getBy(sql);
    }

    private List<Ticket> getBy(String sql) {
        List<Ticket> items = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Ticket item = this.buildItem(rs);
                items.add(item);
            }
            logger.info(String.format("Items from table=%s were successfully gotten.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't get items from %s, see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
        return items;
    }

}
