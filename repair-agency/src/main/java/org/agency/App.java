package org.agency;

import org.agency.entity.Session;
import org.agency.service.AuthService;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // TODO add tests coverage
        // TODO add logger

        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        while (true) {
            System.out.println("1 - login");
            System.out.println("2 - logiout");
            System.out.println("3 - register");

            int choice = scanner.nextInt();
            if (choice == 1) {
                String email = scanner.nextLine();
                String password = scanner.nextLine();
                boolean operationStatus = authService.login(email, password);
                if (operationStatus) {
                    System.out.println("Successfully authorised!");
                } else {
                    System.out.println("Oops, try again..."); // TODO only 3 times
                    // TODO cached information for the password
                    // TODO to cache everything that is possible
                }
            } else if (choice == 2) {
                Session session = authService.getCurrentSession();
                authService.logout(session.getEmail());
            } else if (choice == 3) {
                // TODO imlement registration
            } else {
                // TODO clear session
                break;
            }

        }

    }
}
