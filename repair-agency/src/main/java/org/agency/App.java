package org.agency;

import org.agency.controller.UserController;
import org.agency.entity.Role;
import org.agency.entity.Ticket;
import org.agency.repository.feedback.FeedbackRepository;
import org.agency.repository.ticket.TicketRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.auth.AuthService;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // TODO add tests coverage
        // TODO add DTOs
        // TODO cached information for the password
        // TODO to cache everything that is possible
        // TODO create class with getting fields (for password ask about the same password twice)

        final String DB_URL = System.getenv("PG_DB_URL");
        final String USERNAME = System.getenv("PG_USERNAME");
        final String PASSWORD = System.getenv("PG_PASSWORD");
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connection was successful!");

            TicketRepository ticketRepository = new TicketRepository(connection);
            UserRepository userRepository = new UserRepository(connection);
            FeedbackRepository feedbackRepository = new FeedbackRepository(connection);

            TicketService ticketService = new TicketService(ticketRepository, null);
            UserService userService = new UserService(userRepository);
            FeedbackService feedbackService = new FeedbackService(feedbackRepository);

            UserController userController = new UserController(userService, ticketService, feedbackService);

            Ticket ticket = new Ticket.TicketBuilder("title", "description")
                    .build();

            userController.createTicket(ticket);
            logger.info("ticket was successfully created!");
        } catch (SQLException e) {
            logger.error("ticket was not created! See: " + e);
            throw new RuntimeException(e);
        }

        AuthService authService = new AuthService();
        while (true) {

            // choose a base options for not authorised users (default ones) (login/signup)
            //         or see default ones for authorised (see list of operations / logout)
            // see list of actions
            // check if action is valid for current one
            // perform operation + see result

            System.out.println("Choose your role for the action");
            System.out.println("1 - master");
            System.out.println("2 - user");
            System.out.println("3 - session");

            String choice = scanner.nextLine();
            authService.setAuthorisation(Role.getRoleByName(choice));

            break;

        }

    }

    // re-design this method ..... stupid
    private static String getUserEmail() {
        String input = "";
        System.out.print("Please, enter your email: ");
        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
            // if input is not correct email -> throw Exception
        } else {
            scanner.next();
            System.err.println("Please specify the correct email");
            return getUserEmail();
        }
        return input;
    }

}
