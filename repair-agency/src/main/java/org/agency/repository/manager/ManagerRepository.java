package org.agency.repository.manager;

import org.agency.entity.Manager;
import org.agency.repository.BaseRepository;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerRepository extends BaseRepositoryImpl<Manager> implements PersonRepository<Manager> {

    public ManagerRepository(Connection connection) {
        super(connection, "managers");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
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

}
