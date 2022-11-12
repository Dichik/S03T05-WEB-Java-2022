package org.agency.repository.master;

import org.agency.entity.Master;
import org.agency.exception.TableCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MasterRepositoryImpl implements MasterRepository {
    private static final Logger logger = LogManager.getLogger(MasterRepositoryImpl.class);

    private final Connection connection;

    public MasterRepositoryImpl(Connection connection) {
        this.connection = connection;
        this.createTable();
    }

    private void createTable() {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS masters(" +
                    "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "firstName VARCHAR(255), " +
                    "secondName VARCHAR(255), " +
                    "email VARCHAR(255))";
            statement.executeUpdate(sql);
            logger.info("Table [tickets] was successfully created/updated.");
        } catch (SQLException e) {
            logger.error("Couldn't create table [tickets], see: " + e);
            throw new TableCreationException("Couldn't create table [tickets], see: " + e);
        }
    }

    @Override
    public List<Master> findAll() {
        return null;
    }

    @Override
    public Master findById(Long id) {
        return null;
    }

    @Override
    public void create(Master master) {

    }

    @Override
    public void update(Master master) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Master findByEmail(String email) {
        return null;
    }

}
