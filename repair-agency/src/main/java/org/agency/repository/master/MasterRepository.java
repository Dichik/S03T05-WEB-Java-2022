package org.agency.repository.master;

import org.agency.entity.Master;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.DaoImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

/**
 * FIXME take names from properties
 */
@Repository
public class MasterRepository extends DaoImpl<Master> implements PersonRepository<Master> {
    private static final Logger logger = LogManager.getLogger(MasterRepository.class);

    @Autowired
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
    public String getInsertSQLQuery() {
        return "INSERT INTO " + this.tableName +
                " (email, password) " +
                "VALUES (?, ?)";
    }

    @Override
    public Master buildItem(ResultSet rs) throws SQLException {
        return new Master.MasterBuilder(rs.getString("email"),
                rs.getString("password")).build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Master master, boolean setId) throws SQLException {
        if (master.getEmail() != null) {
            ps.setString(1, master.getEmail());
        } else ps.setNull(1, Types.NULL);

        if (master.getPassword() != null) {
            ps.setString(2, master.getPassword());
        } else ps.setNull(2, Types.NULL);

        if (setId) {
            ps.setLong(3, master.getId());
        }

    }

    @Override
    public String getUpdateSQLQuery() {
        return "UPDATE " + this.tableName +
                " SET email=?, password=?" +
                " WHERE id=?";
    }

    @Override
    public Optional<Master> findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName + " WHERE email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            Master item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return Optional.ofNullable(item);
        } catch (SQLException e) {
            String message = String.format("Couldn't get item with email=%s, see: %s", email, e);
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
