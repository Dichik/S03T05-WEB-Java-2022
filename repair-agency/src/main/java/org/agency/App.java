package org.agency;

import org.agency.entity.Role;
import org.agency.service.auth.AuthService;

import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // TODO add tests coverage
        // TODO add logger

        AuthService authService = new AuthService();
        while (true) {

            System.out.println("Choose your role for the action");
            System.out.println("1 - master");
            System.out.println("2 - user");
            System.out.println("3 - session");

            int choice = scanner.nextInt();
            authService.setAuthorisation(Role.getRoleByIndex(choice));



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
