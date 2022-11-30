package org.agency.repository;

import org.agency.exception.SQLOperationException;
import org.agency.exception.TableCreationException;
import org.agency.exception.TableDeletionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepositoryImpl<T> implements BaseRepository<T> {
    private static final Logger logger = LogManager.getLogger(BaseRepositoryImpl.class);

    protected final Connection connection;
    protected final String tableName;

    public BaseRepositoryImpl(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // FIXME add annotation to drop table on delete (use the same name in annotation as in creation)

    public abstract String getTableSQLSchema();

    public abstract T buildItem(ResultSet rs) throws SQLException;

    public abstract void updatePreparedStatementWithItemData(PreparedStatement ps, T item) throws SQLException;

    @Override
    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = this.getTableSQLSchema();
            statement.executeUpdate(sql);
            logger.info(String.format("Table [%s] was successfully created/updated.", this.tableName));
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
            logger.info(String.format("Item from table=%s was successfully gotten.", this.tableName));
        } catch (SQLException e) {
            String message = String.format("Couldn't get %s, see: %s", this.tableName, e);
            throw new SQLOperationException(message);
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
                item = this.buildItem(rs);
            }
            return item;
        } catch (SQLException e) {
            String message = String.format("Couldn't get ticket with id=%d, see: %s", id, e);
            throw new SQLOperationException(message);
        }
    }

//    @Override
//    public void create(T item) {
//        // FIXME broken insert statement, should be generic
//        String sql = "INSERT INTO " + this.tableName + " (title, description, status, masterId, price, createdAt) VALUES (?, ?, ?, ?, ?, ?) ";
//        try (PreparedStatement ps = connection.prepareStatement(sql)) {
//            updatePreparedStatementWithItemData(ps, item);
//            ps.execute();
//        } catch (SQLException e) {
//            String message = String.format("Couldn't create item in table=[%s], see: %s", this.tableName, e);
//            throw new SQLOperationException(message);
//        }
//    }

    @Override
    public void update(Long id, T item) {
        String sql = "UPDATE " + this.tableName +
                " SET title=?, description=?, status=?, masterId=?, price=?, createdAt=?" +
                " WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            updatePreparedStatementWithItemData(ps, item);
            ps.setLong(7, id);
            ps.execute();
        } catch (SQLException e) {
            String message = String.format("Couldn't update item in table=[%s], see: %s", this.tableName, e);
            throw new SQLOperationException(message);
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
            String message = String.format("Couldn't update item in table=[%s], see: %s", this.tableName, e);
            throw new SQLOperationException(message);
        }
    }

}
