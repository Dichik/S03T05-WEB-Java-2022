package org.agency.service.operation.performer;

import org.agency.service.operation.ActionPerformer;

import java.util.Arrays;
import java.util.Scanner;

public class DefaultPerformer implements ActionPerformer {

    private static final String[] actions = new String[] {
            "login",
            "register",
            "exit"
    };
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void showActions() {
        for (int i = 0; i < actions.length; ++ i) {
            System.out.println("Enter[" + i + "] to " + actions[i]);
        }
    }

    @Override
    public String chooseValidAction() {
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter string action name. Please try again.");
            scanner.next();
        }
        String input = scanner.nextLine();
        if (!validateInputAction(input)) {
            throw new RuntimeException("Action is not valid!");
        }
        return input;
    }

    private boolean validateInputAction(String input) {
        return Arrays.asList(actions).contains(input);
    }

    @Override
    public void performAction(String action) {

    }

}
