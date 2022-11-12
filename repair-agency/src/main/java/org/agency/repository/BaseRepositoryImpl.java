package org.agency.repository;

import org.agency.entity.Ticket;
import org.agency.exception.TableCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {
    private static final Logger logger = LogManager.getLogger(BaseRepositoryImpl.class);

    protected Connection connection;
    protected String tableName;

    // FIXME add annotation to drop table on delete (use the same name in annotation as in creation)

    abstract String getTableName();

    abstract Connection getConnection();

    abstract String getTableSQLSchema();

    // FIXME add creation annotation before constructor creation
    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS " + this.tableName + "(" +
//                    "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
//                    "title VARCHAR(255), " +
//                    "description VARCHAR(255), " +
//                    "status VARCHAR(255), " +
//                    "masterId BIGINT, " +
//                    "price DECIMAL DEFAULT NULL, " +
//                    "createdAt TIMESTAMP)";
            String sql = this.getTableSQLSchema();
            statement.executeUpdate(sql);
            logger.info(String.format("Table [%s] was successfully created/updated.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't create table [%s], see: %s", this.tableName, e);
            logger.error(message); // FIXME logger level up
            throw new TableCreationException(message);
        }
    }

    @Override
    public void dropTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "DROP TABLE IF EXISTS " + this.tableName;
            statement.executeUpdate(sql);
            logger.info(String.format("Table [%s] was successfully dropped.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't drop table [%s], see: %s", this.tableName, e);
            logger.error(message); // FIXME
            throw new TableCreationException(message);
        }
    }

    @Override
    public List<T> findAll() {
        List<T> items = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from tickets"; // FIXME add method to get database name (get from ???)
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
//                T item = new T();
//                items.add(ticket);
                // FIXME add getDescriptionMethod for each entity (toString override)
            }
            logger.info("Tickets was successfully gotten.");
        } catch (SQLException e) {
            logger.error("Couldn't get tickets, see: " + e);
            throw new TableCreationException("Couldn't get tickets, see: " + e);
        }
        return items;
    }

    @Override
    public T findById(Long id) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from " + this.tableName;
            ResultSet rs = statement.executeQuery(sql);
            T item = null;
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
            }
            return item;
        } catch (SQLException e) {
            logger.error(String.format("Couldn't get ticket with id=%d, see: %s", id, e));
            throw new TableCreationException(String.format("Couldn't get ticket with id=%d, see: %s", id, e));
        }
    }

    @Override
    public void create(T ticket) {
        String sql = "INSERT INTO tickets(title, description, status, masterId, price, createdAt) VALUES (?, ?, ?, ?, ?, ?) ";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            updatePrepareStatementWithTicketData(ps, ticket);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

    @Override
    public void update(T ticket) {
        String sql = "UPDATE " + this.tableName +
                " SET title=?, description=?, status=?, masterId=?, price=?, createdAt=?" +
                " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            updatePrepareStatementWithTicketData(ps, ticket);
//            ps.setLong(7, ticket.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM " + this.tableName +
                " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e); // FIXME
        }
    }

}
