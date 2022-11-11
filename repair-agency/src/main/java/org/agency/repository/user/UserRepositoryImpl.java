package org.agency.repository.user;

import org.agency.entity.Ticket;
import org.agency.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, email VARCHAR(255), money DECIMAL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public List<Ticket> findAll() {
        return null;
    }

    @Override
    public Ticket findById(Long id) {
        return null;
    }

    @Override
    public void create(User t) {
    }

    @Override
    public void update(User t) {
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

}
