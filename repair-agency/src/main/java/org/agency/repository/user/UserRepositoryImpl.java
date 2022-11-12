package org.agency.repository.user;

import org.agency.entity.User;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;

import java.sql.*;

public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements PersonRepository<User> {

    public UserRepositoryImpl(Connection connection) {
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

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

}
