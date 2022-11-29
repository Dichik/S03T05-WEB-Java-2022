package org.agency.repository.master;

import org.agency.entity.Master;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MasterRepository extends BaseRepositoryImpl<Master> implements PersonRepository<Master> {
    private static final Logger logger = LogManager.getLogger(MasterRepository.class);

    public MasterRepository(Connection connection) {
        super(connection, "masters");

        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS masters(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "firstName VARCHAR(255), " +
                "secondName VARCHAR(255), " +
                "email VARCHAR(255), " +
                "password VARCHAR(255))";
    }

    @Override
    public Master buildItem(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Master item) throws SQLException {

    }

    @Override
    public Master findByEmail(String email) {
        return null;
    }

}
