package org.agency.repository.ticket;

import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.exception.SQLOperationException;
import org.agency.repository.BaseRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepository extends BaseRepositoryImpl<Ticket> {
    private static final Logger logger = LogManager.getLogger(TicketRepository.class);

    public TicketRepository(Connection connection) {
        super(connection, "tickets");

//        this.dropTable();
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
                "masterId BIGINT, " +
                "price DECIMAL DEFAULT NULL, " +
                "createdAt TIMESTAMP)";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (title, description, userEmail, status, masterId, price, createdAt) " +
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
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Ticket item) throws SQLException {
        ps.setString(1, item.getTitle());
        ps.setString(2, item.getDescription());
        ps.setString(3, item.getUserEmail());
        ps.setString(4, item.getStatus().getName());

        if (item.getMasterEmail() != null) {
            ps.setString(5, item.getMasterEmail());
        } else ps.setNull(5, Types.NULL);

        ps.setBigDecimal(6, item.getPrice());
        ps.setTimestamp(7, item.getCreatedAt());
    }

    public List<Ticket> getByUserEmail(String email) {
        List<Ticket> items = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from " + this.tableName + " WHERE userEmail='" + email + "'";
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

    public List<Ticket> getByMasterEmail(String email) {
        List<Ticket> items = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from " + this.tableName + " WHERE masterEmail='" + email + "'";
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
