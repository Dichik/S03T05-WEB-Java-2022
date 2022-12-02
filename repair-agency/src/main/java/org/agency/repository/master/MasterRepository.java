package org.agency.repository.master;

import org.agency.entity.Master;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.BaseRepositoryImpl;
import org.agency.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MasterRepository extends BaseRepositoryImpl<Master> implements PersonRepository<Master> {
    private static final Logger logger = LogManager.getLogger(MasterRepository.class);

    public MasterRepository(Connection connection) {
        super(connection, "masters"); // FIXME name must be taken from properties of somewhere else

        this.createTable();
    }
// FIXME why do we need first name and second name? Maybe we can easily remove them? Keep it simple stupid (KISS)
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
                " (firstName, secondName, email, password) " +
                "VALUES (?, ?, ?, ?)";
    }

    @Override
    public Master buildItem(ResultSet rs) throws SQLException {
        // FIXME first name and second name
        // TODO should we add balance for master???

        return new Master.MasterBuilder(
                rs.getString("email"),
                rs.getString("password")
        ).build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Master master, boolean setId) throws SQLException {
        if (master.getFirstName() != null) {
            ps.setString(1, master.getFirstName());
        } else ps.setNull(1, Types.NULL);

        if (master.getSecondName() != null) {
            ps.setString(2, master.getSecondName());
        } else ps.setNull(2, Types.NULL);

        if (master.getEmail() != null) {
            ps.setString(3, master.getEmail());
        } else ps.setNull(3, Types.NULL);

        if (master.getPassword() != null) {
            ps.setString(4, master.getPassword());
        } else ps.setNull(4, Types.NULL);
    }

    @Override
    public String getUpdateSQLQuery() {
        return null;
    }

    @Override
    public Master findByEmail(String email) throws EntityNotFoundException {
        try {
            Statement statement = this.connection.createStatement();
            String sql = "SELECT * FROM " + this.tableName + " WHERE email='" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            Master item = null;
            while (rs.next()) {
                item = this.buildItem(rs);
            }
            return item;
        } catch (SQLException e) {
            String message = String.format("Couldn't get item with email=%s, see: %s", email, e);
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

}
