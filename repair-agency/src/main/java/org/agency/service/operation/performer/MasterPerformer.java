package org.agency.service.operation.performer;

import org.agency.controller.MasterController;
import org.agency.entity.TicketStatus;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.MasterAction;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class MasterPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(MasterPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private final MasterController masterController;


    public MasterPerformer(MasterController masterController) {
        this.masterController = masterController;
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
                    this.masterController.getTicketsByEmail(CurrentSession.getSession().getEmail())
            ); // FIXME

            System.out.println("Enter ticket id: ");
            while (!scanner.hasNextLong()) {
                System.out.println("You should enter valid long id. Please try again.");
                scanner.next();
            }
            Long id = scanner.nextLong();

            System.out.println("Enter status: ");
            System.out.printf("Valid options=%s%n", List.of(TicketStatus.IN_PROGRESS, TicketStatus.DONE));
            while (!scanner.hasNextLine()) {
                System.out.println("You should enter valid string action name. Please try again.");
                scanner.next();
            }
            String status = scanner.nextLine();

            this.masterController.updateStatus(id, status);
        } else if (masterAction == MasterAction.LOGOUT) {
            this.masterController.logout();
            logger.info("Action " + masterAction.getName() + " was successfully performed.");
            return false;
        }
        return true;
    }
}
