package org.agency.service.operation.performer;

import org.agency.entity.Role;
import org.agency.exception.EntityNotFoundException;
import org.agency.service.auth.AuthService;
import org.agency.delegator.ServiceDelegator;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.DefaultAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DefaultPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(DefaultPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;

    public DefaultPerformer(ServiceDelegator serviceDelegator) {
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    @Override
    public void showActions() {
        for (DefaultAction action : DefaultAction.values()) {
            System.out.println("Enter [" + action.getName() + "] to perform.");
        }
    }

    @Override
    public Action chooseValidAction() {
        while (!scanner.hasNextLine()) {
            System.out.println("You should enter string action name. Please try again.");
            scanner.next();
        }
        String input = scanner.nextLine();
        return DefaultAction.valueOf(input.toUpperCase());
    }

    @Override
    public void performAction(Action action) {
        DefaultAction defaultAction = (DefaultAction) action;
        if (defaultAction == DefaultAction.LOGIN) {

            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid email. Please try again.");
                scanner.next();
            }
            String email = scanner.nextLine();

            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid password. Please try again.");
                scanner.next();
            }
            String password = scanner.nextLine();

            try {
                this.authService.login(email, password, Role.USER); // FIXME
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

            logger.info("Login action was performed.");
        } else if (defaultAction == DefaultAction.REGISTER) {

            // FIXME register as Master, User (default password for admin);

            System.out.println("Enter email: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid email. Please try again.");
                scanner.next();
            }
            String email = scanner.nextLine();

            System.out.println("Enter password: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid password. Please try again.");
                scanner.next();
            }
            String password1 = scanner.nextLine();

            System.out.println("Enter same password again: ");
            String password2;
            while (!scanner.hasNextLine() && !(password2 = scanner.nextLine()).equals(password1)) {
                System.out.println("You should enter valid password one more time. Please try again.");
                scanner.next();
            }

            this.authService.register(email, password1, Role.USER); // FIXME

            logger.info("Perform register action.");
        } else if (defaultAction == DefaultAction.EXIT) {
            logger.info("Exit action performed.");
            System.exit(1);
        } else {
            throw new RuntimeException();
        }
    }

}
