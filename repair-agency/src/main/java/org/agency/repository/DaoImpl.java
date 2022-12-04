package org.agency.repository;

import org.agency.exception.SQLOperationException;
import org.agency.exception.TableCreationException;
import org.agency.exception.TableDeletionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DaoImpl<T> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(DaoImpl.class);

    protected final Connection connection;
    protected final String tableName;

    public DaoImpl(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public abstract String getTableSQLSchema();

    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = this.getTableSQLSchema();
            statement.executeUpdate(sql);
            logger.info(String.format("Table [%s] was successfully created.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't create table [%s], see: %s", this.tableName, e);
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
            throw new TableDeletionException(message);
        }
    }

    public abstract T buildItem(ResultSet rs) throws SQLException;

    @Override
    public List<T> findAll() {
        List<T> items = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from " + this.tableName;
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                T item = this.buildItem(rs);
                items.add(item);
            }
            logger.info(String.format("Items from table=%s were successfully gotten.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't get items from %s, see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
        return items;
    }

    @Override
    public Optional<T> findById(Long id) {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * from " + this.tableName;
            ResultSet rs = statement.executeQuery(sql);
            T item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return item != null ? Optional.of(item) : Optional.empty();
        } catch (SQLException e) {
            String message = String.format("Couldn't get ticket with id=%d, see: %s", id, e);
            throw new SQLOperationException(message);
        }
    }

    public abstract String getInsertSQLQuery();

    public abstract void updatePreparedStatementWithItemData(PreparedStatement ps, T item, boolean setId) throws SQLException;

    @Override
    public void create(T t) {
        String sql = this.getInsertSQLQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            updatePreparedStatementWithItemData(ps, t, false);
            ps.execute();
            logger.info("Item in table=[" + this.tableName + "] was successfully created.");
        } catch (SQLException e) {
            String message = String.format("Couldn't create item in table=[%s], see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
    }

    public abstract String getUpdateSQLQuery();

    @Override
    public void update(Long id, T item) {
        String sql = this.getUpdateSQLQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            updatePreparedStatementWithItemData(ps, item, true);
            int result = ps.executeUpdate();
            if (result == 0) {
                logger.warn(String.format("Haven't found item with id=[%s] to update.", id));
            } else {
                logger.info(String.format("Item with id=[%d] was successfully updated.", id));
            }
        } catch (SQLException e) {
            String message = String.format("Couldn't update item in table=[%s], see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM " + this.tableName + " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            String message = String.format("Couldn't update item in table=[%s], see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
    }

}
