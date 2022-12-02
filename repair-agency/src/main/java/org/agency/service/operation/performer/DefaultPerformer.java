package org.agency.service.operation.performer;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Role;
import org.agency.exception.EntityNotFoundException;
import org.agency.service.auth.AuthService;
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
            System.out.println("You should enter valid string action name. Please try again.");
            scanner.next();
        }
        String input = scanner.nextLine();
        return DefaultAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        DefaultAction defaultAction = (DefaultAction) action;
        if (defaultAction == DefaultAction.LOGIN) {

            System.out.println("Valid roles: ");
            for (Role role: Role.values()) {
                if (role != Role.NOT_AUTHORIZED) {
                    System.out.println("Role=[" + role + "]");
                }
            }
            System.out.println("Enter role: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid email. Please try again.");
                scanner.next();
            }
            Role role = Role.valueOf(scanner.nextLine().toUpperCase()); // FIXME try to enter one more time instead of exit

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
            String password = scanner.nextLine();

            try {
                this.authService.login(email, password, role);
                logger.info("Login action was performed.");
                return true;
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (defaultAction == DefaultAction.REGISTER) {

            // FIXME register as Master, User (default password for admin);
            // TODO add email validation
            System.out.println("Valid roles: ");
            for (Role role: Role.values()) {
                if (role != Role.NOT_AUTHORIZED) {
                    System.out.println("Role=[" + role + "]");
                }
            }

            System.out.println("Enter role: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid email. Please try again.");
                scanner.next();
            }
            Role role = Role.valueOf(scanner.nextLine().toUpperCase());

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
            String password = scanner.nextLine();

            System.out.println("Enter same password again: ");
            while (!scanner.nextLine().equals(password)) {
                System.out.println("You should enter valid password one more time. Please try again.");
                scanner.next();
            }

            this.authService.register(email, password, role);
            logger.info("Perform register action.");
            return true;
        }
        // FIXME should we perform exit only in case if we have input EXIT?
        logger.info("Exit action performed.");
        return false;
    }

}
