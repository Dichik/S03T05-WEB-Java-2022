package org.agency.repository.ticket;

import org.agency.entity.Ticket;
import org.agency.repository.BaseRepositoryImpl;

import java.sql.*;

public class TicketRepository extends BaseRepositoryImpl<Ticket> {

    public TicketRepository(Connection connection) {
        super(connection, "tickets");

        this.dropTable();
        this.createTable();
    }

    @Override
    public String getTableSQLSchema() {
        return "CREATE TABLE IF NOT EXISTS " + this.tableName + "(" +
                "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "title VARCHAR(255), " +
                "description VARCHAR(255), " +
                "status VARCHAR(255), " +
                "masterId BIGINT, " +
                "price DECIMAL DEFAULT NULL, " +
                "createdAt TIMESTAMP)";
    }

    @Override
    public Ticket buildItem(ResultSet rs) throws SQLException {
        return Ticket.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(rs.getString("status"))
                .masterId(rs.getLong("masterId"))
                .price(rs.getBigDecimal("price"))
                .createdAt(rs.getTimestamp("createAt"))
                .build();
    }

    @Override
    public void updatePreparedStatementWithItemData(PreparedStatement ps, Ticket item) throws SQLException {
        ps.setString(1, item.getTitle());
        ps.setString(2, item.getDescription());
        ps.setString(3, item.getStatus());

        if (item.getMasterId() != null) {
            ps.setLong(4, item.getMasterId());
        } else ps.setNull(4, Types.NULL);

        ps.setBigDecimal(5, item.getPrice());
        ps.setTimestamp(6, item.getCreatedAt());
    }

}
