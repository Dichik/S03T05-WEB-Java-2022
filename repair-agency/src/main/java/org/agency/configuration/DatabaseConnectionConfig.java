package org.agency.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConnectionConfig {
    private static final Logger logger = LogManager.getLogger(DatabaseConnectionConfig.class);

    @Bean
    public Connection connection() {
        final String DB_URL = System.getenv("PG_DB_URL");
        final String USERNAME = System.getenv("PG_USERNAME");
        final String PASSWORD = System.getenv("PG_PASSWORD");

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            logger.info("Connection was successfully created!");
            return connection;
        } catch (SQLException e) {
            logger.error("Error while trying to connect to database, see: " + e);
            throw new RuntimeException(e);
        }
    }

}
