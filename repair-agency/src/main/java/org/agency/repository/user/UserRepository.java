package org.agency.repository.user;

import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserRepository extends BaseRepositoryImpl<User> implements PersonRepository<User> {
    private static final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository(Connection connection) {
        super(connection, "users");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " +
                this.tableName +
                " (id INTEGER PRIMARY KEY, " +
                "email VARCHAR(255), " +
                "money DECIMAL)";
    }

    @Override
    public User buildItem(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, User item) throws SQLException {
        ps.setString(1, item.getTitle());
    }

    @Override
    public User findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName +
                    " WHERE email=" + email;
            ResultSet rs = statement.executeQuery(sql);
            User item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return item;
        } catch (SQLException e) {
            String message = String.format("Couldn't get user with email=%s, see: %s", email, e);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

}
