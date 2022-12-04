package org.agency.repository.user;

import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.DaoImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserRepository extends DaoImpl<User> implements PersonRepository<User> {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository(Connection connection) {
        super(connection, "users");

//        this.dropTable();
        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " +
                this.tableName +
                " (id SERIAL PRIMARY KEY, " +
                "firstName VARCHAR(255), " +
                "secondName VARCHAR(255), " +
                "email VARCHAR(255), " +
                "balance decimal, " +
                "password VARCHAR(255))";
    }

    @Override
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (firstName, secondName, email, balance, password) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    // FIXME field names should be in configuration
    @Override
    public User buildItem(ResultSet rs) throws SQLException {
        return new User.UserBuilder(rs.getString("email"))
                .setId(rs.getLong("id"))
                .setFirstName(rs.getString("firstName"))
                .setSecondName(rs.getString("secondName"))
                .setPassword(rs.getString("password"))
                .build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, User user, boolean setId) throws SQLException {
        if (user.getFirstName() != null) {
            ps.setString(1, user.getFirstName());
        } else ps.setNull(1, Types.NULL);

        if (user.getSecondName() != null) {
            ps.setString(2, user.getSecondName());
        } else ps.setNull(2, Types.NULL);

        if (user.getEmail() != null) {
            ps.setString(3, user.getEmail());
        } else ps.setNull(3, Types.NULL);

        ps.setBigDecimal(4, user.getBalance());

        if (user.getPassword() != null) {
            ps.setString(5, user.getPassword());
        } else ps.setNull(5, Types.NULL);

        if (setId) {
            ps.setLong(6, user.getId());
        }

    }

    @Override
    public String getUpdateSQLQuery() {
        return "UPDATE " + this.tableName +
                " SET firstName=?, secondName=?, email=?, balance=?, password=?" +
                " WHERE id=?";
    }

    @Override
    public User findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName + " WHERE email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            User item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return item;
        } catch (SQLException e) {
            String message = String.format("Couldn't get user with item=%s, see: %s", email, e);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

}
