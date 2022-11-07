package org.agency.repository;

import org.agency.entity.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TicketRepository {
    private final Logger logger = LogManager.getLogger(TicketRepository.class);

    private final Connection connection;

    public TicketRepository(Connection connection) {
        this.connection = connection;
        this.createTable();
    }

    private void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS tickets(id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(255), description VARCHAR(255), price DECIMAL DEFAULT NULL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets(title, description) VALUES (?, ?) ";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, ticket.getTitle());
            ps.setObject(2, ticket.getDescription());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
