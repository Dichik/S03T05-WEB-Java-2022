package org.agency;

import org.agency.controller.UserController;
import org.agency.entity.Role;
import org.agency.entity.Ticket;
import org.agency.repository.feedback.FeedbackRepository;
import org.agency.repository.feedback.FeedbackRepositoryImpl;
import org.agency.repository.ticket.TicketRepository;
import org.agency.repository.ticket.TicketRepositoryImpl;
import org.agency.repository.user.UserRepository;
import org.agency.repository.user.UserRepositoryImpl;
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

        final String DB_URL = System.getenv("PG_DB_URL");
        final String USERNAME = System.getenv("PG_USERNAME");
        final String PASSWORD = System.getenv("PG_PASSWORD");
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connection was successful!");

            TicketRepository ticketRepository = new TicketRepositoryImpl(connection);
            UserRepository userRepository = new UserRepositoryImpl(connection);
            FeedbackRepository feedbackRepository = new FeedbackRepositoryImpl(connection);

            TicketService ticketService = new TicketService(ticketRepository);
            UserService userService = new UserService(userRepository);
            FeedbackService feedbackService = new FeedbackService(feedbackRepository);

            UserController userController = new UserController(userService, ticketService, feedbackService);

            Ticket ticket = Ticket.builder()
                    .title("title")
                    .description("description")
                    .build();

            userController.createTicket(ticket);
            logger.info("ticket was successfully created!");
        } catch (SQLException e) {
            logger.error("ticket was not created! See: " + e);
            throw new RuntimeException(e);
        }

        AuthService authService = new AuthService();
        while (true) {

            System.out.println("Choose your role for the action");
            System.out.println("1 - master");
            System.out.println("2 - user");
            System.out.println("3 - session");

            int choice = scanner.nextInt();
            authService.setAuthorisation(Role.getRoleByIndex(choice));

            break;

//            System.out.println("1 - login");
//            System.out.println("2 - logout");
//            System.out.println("3 - register");
//            int choice = scanner.nextInt();
//            if (choice == 1) {
//                System.out.print("Email: ");
//                String email = scanner.nextLine();
//                System.out.print("Password: ");
//                String password = scanner.nextLine();
//                boolean operationStatus = authService.login(email, password);
//                if (operationStatus) {
//                    System.out.println("Successfully authorised!");
//                } else {
//                    System.out.println("Oops, try again..."); // TODO only 3 times
//                    // TODO cached information for the password
//                    // TODO to cache everything that is possible
//                }
//            } else if (choice == 2) {
//                authService.logout();
//            } else if (choice == 3) {
//
//                String email = getUserEmail();
//                String password = getUserEmail(); // TODO create class with getting fields (for password ask about the same password twice)
//
//                authService.register(email, password);
//            } else {
//                // TODO clear session
//                break;
//            }

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
