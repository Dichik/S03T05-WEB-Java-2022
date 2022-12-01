package org.agency.repository.manager;

import org.agency.entity.Manager;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerRepository extends BaseRepositoryImpl<Manager> implements PersonRepository<Manager> {

    public ManagerRepository(Connection connection) {
        super(connection, "managers"); // FIXME take name from the correct place

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS managers(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "firstName VARCHAR(255), " +
                "secondName VARCHAR(255), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255))";
    }

    @Override
    public String getInsertSQLQuery() {
        return null;
    }

    @Override
    public Manager buildItem(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Manager item) throws SQLException {

    }

    @Override
    public Manager findByEmail(String email) {
        return null;
    }

    @Override
    public void create(Manager manager) {

    }
}
