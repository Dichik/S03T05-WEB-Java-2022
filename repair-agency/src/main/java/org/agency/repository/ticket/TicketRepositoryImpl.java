package org.agency.repository.ticket;

import org.agency.entity.Ticket;
import org.agency.exception.TableCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {
    private final Logger logger = LogManager.getLogger(TicketRepositoryImpl.class);

    private final Connection connection;

    public TicketRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.dropTable();
        this.createTable();
    }

    private void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS tickets(" +
                    "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "title VARCHAR(255), " +
                    "description VARCHAR(255), " +
                    "status VARCHAR(255), " +
                    "masterId BIGINT, " +
                    "price DECIMAL DEFAULT NULL, " +
                    "createdAt TIMESTAMP)";
            statement.executeUpdate(sql);
            logger.info("Table [tickets] was successfully created/updated.");
        } catch (SQLException e) {
            logger.error("Couldn't create table [tickets], see: " + e);
            throw new TableCreationException("Couldn't create table [tickets], see: " + e);
        }
    }

    private void dropTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "DROP TABLE IF EXISTS tickets";
            statement.executeUpdate(sql);
            logger.info("Table [tickets] was successfully dropped.");
        } catch (SQLException e) {
            logger.error("Couldn't drop table [tickets], see: " + e);
            throw new TableCreationException("Couldn't drop table [tickets], see: " + e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from tickets";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Ticket ticket = Ticket.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .status(rs.getString("status"))
                        .masterId(rs.getLong("masterId"))
                        .price(rs.getBigDecimal("price"))
                        .createdAt(rs.getTimestamp("createAt"))
                        .build();
                tickets.add(ticket);
            }
            logger.info("Tickets was successfully gotten.");
        } catch (SQLException e) {
            logger.error("Couldn't get tickets, see: " + e);
            throw new TableCreationException("Couldn't get tickets, see: " + e);
        }
        return tickets;
    }

    @Override
    public Ticket findById(Long id) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from tickets";
            ResultSet rs = statement.executeQuery(sql);
            Ticket ticket = null;
            while (rs.next()) {
                ticket = Ticket.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .status(rs.getString("status"))
                        .masterId(rs.getLong("masterId"))
                        .price(rs.getBigDecimal("price"))
                        .createdAt(rs.getTimestamp("createAt"))
                        .build();
            }
            return ticket;
        } catch (SQLException e) {
            logger.error(String.format("Couldn't get ticket with id=%d, see: %s", id, e));
            throw new TableCreationException(String.format("Couldn't get ticket with id=%d, see: %s", id, e));
        }
    }

    @Override
    public void create(Ticket ticket) {
        String sql = "INSERT INTO tickets(title, description, status, masterId, price, createdAt) VALUES (?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            updatePrepareStatementWithTicketData(ps, ticket);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

    @Override
    public void update(Ticket ticket) {
        String sql = "UPDATE tickets " +
                "set title=?, description=?, status=?, masterId=?, price=?, createdAt=?" +
                "WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            updatePrepareStatementWithTicketData(ps, ticket);
            ps.setLong(7, ticket.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM tickets " +
                "WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

    private void updatePrepareStatementWithTicketData(PreparedStatement ps, Ticket ticket) throws SQLException {
        ps.setString(1, ticket.getTitle());
        ps.setString(2, ticket.getDescription());
        ps.setString(3, ticket.getStatus());

        if (ticket.getMasterId() != null) {
            ps.setLong(4, ticket.getMasterId());
        } else ps.setNull(4, Types.NULL);

        ps.setBigDecimal(5, ticket.getPrice());
        ps.setTimestamp(6, ticket.getCreatedAt());
    }

}
