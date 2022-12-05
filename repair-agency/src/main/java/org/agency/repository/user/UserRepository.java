package org.agency.repository.user;

import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.DaoImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class UserRepository extends DaoImpl<User> implements PersonRepository<User> {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository(Connection connection) {
        super(connection, "users");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " +
                this.tableName +
                " (id SERIAL PRIMARY KEY, " +
                "email VARCHAR(255), " +
                "balance decimal, " +
                "password VARCHAR(255))";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (email, balance, password) " +
                "VALUES (?, ?, ?)";
    }

    @Override
    public User buildItem(ResultSet rs) throws SQLException {
        return new User.UserBuilder(rs.getString("email"))
                .setId(rs.getLong("id"))
                .setPassword(rs.getString("password"))
                .setBalance(rs.getBigDecimal("balance"))
                .build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, User user, boolean setId) throws SQLException {
        if (user.getEmail() != null) {
            ps.setString(1, user.getEmail());
        } else ps.setNull(1, Types.NULL);

        ps.setBigDecimal(2, user.getBalance());

        if (user.getPassword() != null) {
            ps.setString(3, user.getPassword());
        } else ps.setNull(3, Types.NULL);

        if (setId) {
            ps.setLong(4, user.getId());
        }

    }

    @Override
    public String getUpdateSQLQuery() {
        return "UPDATE " + this.tableName +
                " SET email=?, balance=?, password=?" +
                " WHERE id=?";
    }

    @Override
    public Optional<User> findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName + " WHERE email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            User item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return Optional.ofNullable(item);
        } catch (SQLException e) {
            String message = String.format("Couldn't get user with item=%s, see: %s", email, e);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            this.findByEmail(email);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

}
