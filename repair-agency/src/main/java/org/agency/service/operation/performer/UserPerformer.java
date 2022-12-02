package org.agency.service.operation.performer;

import org.agency.controller.UserController;
import org.agency.entity.Ticket;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.UserAction;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class UserPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(UserPerformer.class);

    private static final Scanner scanner = new Scanner(System.in); // FIXME move logic with getting inputs
    private final UserController userController;

    public UserPerformer(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void showActions() {
        for (UserAction action : UserAction.values()) {
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
        try {
            return UserAction.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = input + " action is not valid.";
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public boolean performAction(Action action) {
        UserAction userAction = (UserAction) action;

        // adapter.perform(userAction)
        // catch error and return true

        if (userAction == UserAction.SUBMIT_TICKET) {
            System.out.println("Enter title: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid string name. Please try again.");
            }
            String title = scanner.nextLine();

            System.out.println("Enter description: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid string name. Please try again.");
            }
            String description = scanner.nextLine();

            Ticket ticket = new Ticket.TicketBuilder(
                    title,
                    description,
                    CurrentSession.getSession().getEmail()
            ).build();
            this.userController.createTicket(ticket);
            logger.info("Action " + userAction.getName() + " was successfully performed.");
            return true;
        } else if (userAction == UserAction.SHOW_MY_TICKETS) {
            List<Ticket> tickets = this.userController.getTicketsByUserEmail(CurrentSession.getSession().getEmail());
            System.out.println(tickets);
            logger.info("Action " + userAction.getName() + " was successfully performed.");
            return true;
        } else if (userAction == UserAction.LOGOUT) {
            this.userController.logout();
            logger.info("Action " + userAction.getName() + " was successfully performed.");
            return true;
        }
        logger.info("Exit action performed.");
        return false;
    }
}
