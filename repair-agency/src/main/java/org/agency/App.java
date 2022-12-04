package org.agency;

import org.agency.controller.ActionController;
import org.agency.delegator.RepositoryDelegator;
import org.agency.delegator.ServiceDelegator;
import org.agency.exception.InvalidActionException;
import org.agency.service.operation.delegator.PerformerDelegator;
import org.agency.service.operation.performer.action.Action;
import org.agency.view.ActionSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    // TODO add tests coverage
    // TODO add DTOs
    // TODO to cache everything that is possible
    // TODO cached information for the password
    // TODO create class with getting fields (for password ask about the same password twice)
    // TODO there is default login and password for admin
    // TODO when do user pay for the request/ticket? after ticket will be successfully done?
    // TODO should we add balance for master???
    // FIXME first_name and second_name remove???

    public static void main(String[] args) {
        logger.info("[App] is started successfully.");

        final String DB_URL = System.getenv("PG_DB_URL");
        final String USERNAME = System.getenv("PG_USERNAME");
        final String PASSWORD = System.getenv("PG_PASSWORD");

        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connection to database was successful!");

            RepositoryDelegator repositoryDelegator = new RepositoryDelegator(connection);
            ServiceDelegator serviceDelegator = new ServiceDelegator(repositoryDelegator);
            PerformerDelegator performerDelegator = new PerformerDelegator(serviceDelegator, new ActionSelector());

            ActionController actionController = new ActionController(performerDelegator);
            boolean next = true;
            while (next) {
                actionController.showActionsList();
                Action action;
                try {
                    action = actionController.chooseAction();
                    next = actionController.performAction(action);
                } catch (InvalidActionException e) {
                    logger.warn("Error occurred, see: " + e);
                }
            }
            logger.info("Action performing is finished.");
        } catch (SQLException e) {
            logger.error("Error occurred, see: " + e);
            throw new RuntimeException(e);
        }
        logger.info("[App] is finished successfully.");
    }

}
