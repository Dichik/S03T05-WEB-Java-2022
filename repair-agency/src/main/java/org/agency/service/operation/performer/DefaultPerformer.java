package org.agency.service.operation.performer;

import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.DefaultAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DefaultPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(DefaultPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void showActions() {
        int index = 0;
        for (DefaultAction action: DefaultAction.values()) {
            System.out.println("Enter[" + (index++) + "] to " + action.getName());
        }
    }

    @Override
    public Action chooseValidAction() {
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter string action name. Please try again.");
            scanner.next();
        }
        String input = scanner.nextLine();
        if (!validateInputAction(input)) {
            throw new RuntimeException("Action is not valid!");
        }
        return Action.getByName(input);
    }

    private boolean validateInputAction(String input) {
        try {
            DefaultAction.getByName(input);
            return true;
        } catch (RuntimeException e) {
            logger.warn("Error, see: " + e);
            return false;
        }
    }

    @Override
    public void performAction(Action action) {
        switch (action) {
            case DefaultAction.LOGIN:
                break;
            case DefaultAction.REGISTER:
                break;
            case DefaultAction.EXIT:
                break;
            default:
                throw new RuntimeException();
        }
    }

}
