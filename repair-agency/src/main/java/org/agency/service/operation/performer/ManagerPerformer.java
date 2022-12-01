package org.agency.service.operation.performer;

import org.agency.controller.ManagerController;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.ManagerAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class ManagerPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(ManagerPerformer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private final ManagerController managerController;

    public ManagerPerformer(ManagerController managerController) {
        this.managerController = managerController;
    }

    @Override
    public void showActions() {
        for (ManagerAction action : ManagerAction.values()) {
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
        return ManagerAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        ManagerAction managerAction = (ManagerAction) action;
        if (managerAction == ManagerAction.ASSIGN_MASTER) {
            Long ticketId = scanner.nextLong();
            String masterEmail = scanner.nextLine();
            this.managerController.assignMasterToTicket(ticketId, masterEmail);
            return false;
        }
        return true;
    }
}
