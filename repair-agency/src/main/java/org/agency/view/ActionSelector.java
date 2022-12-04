package org.agency.view;

import org.agency.entity.Role;
import org.agency.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * TODO there are lots of things that can be improved in this class
 *
 * TODO add email validation
 */

public class ActionSelector {

    public static final String ENTER_EMAIL = "Enter email: ";
    public static final String ENTER_MASTER_EMAIL = "Enter master email: ";
    public static final String ENTER_USER_EMAIL = "Enter user email: ";
    public static final String ENTER_TITLE = "Enter title: ";
    public static final String ENTER_DESCRIPTION = "Enter description: ";
    public static final String ENTER_FEEDBACK = "Enter feedback: ";

    private static final Scanner scanner = new Scanner(System.in);

    public ActionSelector() {

    }

    public void showTickets(List<Ticket> tickets) {
        System.out.println(tickets);
    }


    public String getInput(String message) {
        System.out.println(message);
        return this.getInput();
    }

    public String getInput() {
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid string action name. Please try again.");
            scanner.next();
        }
        return scanner.nextLine();
    }

    public Long getTicketId() {
        System.out.println("Enter ticket id: ");
        while (!scanner.hasNextLong()) {
            System.out.println("You should enter valid string action name. Please try again.");
            scanner.next();
        }
        return Long.parseLong(scanner.nextLine());
    }

    public String getEmail(String message) {
        System.out.println(message);
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid string action name. Please try again.");
            scanner.next();
        }
        return scanner.nextLine();
    }

    public double getAmount() {
        System.out.println("Enter amount: ");
        return Double.parseDouble(scanner.nextLine());
    }

    public String getStatus() {
        System.out.println("Enter status: ");
        return scanner.nextLine(); // FIXME
    }

    public String getPassword() {
        System.out.println("Enter password: ");
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid password. Please try again.");
            scanner.next();
        }
        return scanner.nextLine();
    }

    public void getSamePassword(String password) {
        System.out.println("Enter password: ");
        while (!scanner.nextLine().equals(password)) {
            System.out.println("You should enter valid password one more time. Please try again.");
        }
    }

    public Role getRole() {
        System.out.println("Valid roles: ");
        for (Role role : Role.values()) {
            if (role != Role.NOT_AUTHORIZED) {
                System.out.println("Role=[" + role + "]");
            }
        }

        System.out.println("Enter role: ");
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid email. Please try again.");
            scanner.next();
        }
        return Role.valueOf(scanner.nextLine().toUpperCase()); // FIXME try to enter one more time instead of exit
    }

    public void showBalance(BigDecimal balance) {
        System.out.println("Current balance is " + balance);
    }

}
