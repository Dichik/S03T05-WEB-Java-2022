package org.agency.service.operation.performer;

import org.agency.delegator.ServiceDelegator;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UnvalidStatusUpdateException;
import org.agency.service.auth.AuthService;
import org.agency.service.master.MasterService;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.MasterAction;
import org.agency.service.session.CurrentSession;
import org.agency.service.ticket.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MasterPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(MasterPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private final AuthService authService;
    private final TicketService ticketService;
    private final MasterService masterService;

    public MasterPerformer(ServiceDelegator serviceDelegator) {
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
        this.ticketService = (TicketService) serviceDelegator.getByClass(TicketService.class);
        this.masterService = (MasterService) serviceDelegator.getByClass(MasterService.class);
    }

    @Override
    public void showActions() {
        for (MasterAction action : MasterAction.values()) {
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
        return MasterAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        MasterAction masterAction = (MasterAction) action;
        if (masterAction == MasterAction.CHANGE_STATUS) {

            System.out.println(
                    this.ticketService.getTicketsByMasterEmail(CurrentSession.getSession().getEmail())
            );

            System.out.println("Enter ticket id: ");
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid string action name. Please try again.");
                scanner.next();
            }
            String status = scanner.nextLine();

            System.out.println("Enter status: ");
            while (!scanner.hasNextLong()) {
                System.out.println("You should enter valid long id. Please try again.");
                scanner.next();
            }
            Long id = scanner.nextLong();

            try {
                this.masterService.updateStatus(id, status);
            } catch (EntityNotFoundException | UnvalidStatusUpdateException e) {
                throw new RuntimeException(e);
            }

        } else if (masterAction == MasterAction.LOGOUT) {
            this.authService.logout();
            logger.info("Action " + masterAction.getName() + " was successfully performed.");
            return false;
        }
        return true;
    }
}
