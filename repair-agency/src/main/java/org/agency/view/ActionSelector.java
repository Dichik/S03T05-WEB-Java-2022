package org.agency.view;

import org.agency.entity.Role;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO there are lots of things that can be improved in this class
 */

public class ActionSelector {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final String ENTER_EMAIL = "Enter email: ";
    public static final String ENTER_MASTER_EMAIL = "Enter master email: ";
    public static final String ENTER_USER_EMAIL = "Enter user email: ";
    public static final String ENTER_TITLE = "Enter title: ";
    public static final String ENTER_DESCRIPTION = "Enter description: ";
    public static final String ENTER_FEEDBACK = "Enter feedback: ";

    private static final Scanner scanner = new Scanner(System.in);

    public void showTickets(List<Ticket> tickets) {
        System.out.println(tickets);
    }

    public String getInput(String message) {
        System.out.println(message);
        return this.getInput();
    }

    public String getInput() {
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid string input. Please try again.");
            scanner.next();
        }
        return scanner.nextLine();
    }

    public Long getTicketId() {
        System.out.println("Enter ticket id: ");
        while (!scanner.hasNextLong()) {
            System.out.println("You should enter valid long ticket id. Please try again.");
            scanner.next();
        }
        return scanner.nextLong();
    }

    public String getEmail(String message) {
        System.out.println(message);
        String email;
        while (!scanner.hasNextLine() || !validate(email = scanner.nextLine())) {
            System.out.println("You should enter valid email. Please try again.");
            scanner.next();
        }
        return email;
    }

    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public double getAmount() {
        System.out.println("Enter amount: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("You should enter valid double amount. Please try again.");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    public String getStatus() {
        System.out.println("Valid statuses: ");
        for (TicketStatus status : TicketStatus.values()) {
            System.out.println("Role=[" + status + "]");
        }

        System.out.println("Enter status: ");
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter valid status. Please try again.");
            scanner.next();
        }
        return scanner.nextLine();
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
        System.out.println("Enter same password: ");
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
            System.out.println("You should enter valid role. Please try again.");
            scanner.next();
        }
        return Role.valueOf(scanner.nextLine().toUpperCase());
    }

    public void showBalance(BigDecimal balance) {
        System.out.println("Current balance is " + balance);
    }

}
