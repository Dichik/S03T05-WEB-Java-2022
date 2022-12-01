package org.agency.service.operation.performer;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Ticket;
import org.agency.service.auth.AuthService;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.UserAction;
import org.agency.service.session.CurrentSession;
import org.agency.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class UserPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(UserPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private final TicketService ticketService;
    private final AuthService authService;

    public UserPerformer(ServiceDelegator serviceDelegator) {
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
    }

    @Override
    public void showActions() {
        for (UserAction action: UserAction.values()) {
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
        return UserAction.valueOf(input.toUpperCase()); // FIXME throw exception if input is not valid and handle it on the lever up
    }

    @Override
    public boolean performAction(Action action) {
        UserAction userAction = (UserAction) action;
        if (userAction == UserAction.SUBMIT_TICKET) {
            System.out.println("Enter title: ");
            while(!scanner.hasNextLine()) {
                System.out.println("You should enter valid string name. Please try again.");
            }
            String title = scanner.nextLine();

            System.out.println("Enter description: ");
            while(!scanner.hasNextLine()) {
                System.out.println("You should enter valid string name. Please try again.");
            }
            String description = scanner.nextLine();

            Ticket ticket = new Ticket.TicketBuilder(
                    title,
                    description,
                    CurrentSession.getSession().getEmail()
            ).build();
            this.ticketService.createTicket(ticket);
            logger.info("Action " + userAction.getName() + " was successfully performed.");
        } else if (userAction == UserAction.SHOW_MY_TICKETS) {
            List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(CurrentSession.getSession().getEmail());
            System.out.println(tickets);
            logger.info("Action " + userAction.getName() + " was successfully performed.");
        } else if (userAction == UserAction.LOGOUT) {
            this.authService.logout();
            logger.info("Action " + userAction.getName() + " was successfully performed.");
        } else if (userAction == UserAction.EXIT) {
            logger.info("Action " + userAction.getName() + " was successfully performed.");
            return true;
        } else {
            logger.error("Action " + userAction.getName() + " was not successfully performed.");
            throw new RuntimeException("Error..."); // FIXME
        }
        return false;
    }
}
