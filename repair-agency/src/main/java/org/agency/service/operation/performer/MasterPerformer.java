package org.agency.service.operation.performer;

import org.agency.controller.MasterController;
import org.agency.entity.Ticket;
import org.agency.entity.TicketStatus;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.MasterAction;
import org.agency.service.session.CurrentSession;
import org.agency.view.ActionSelector;

import java.util.List;

public class MasterPerformer implements ActionPerformer {

    private final MasterController masterController;
    private final ActionSelector actionSelector;

    public MasterPerformer(MasterController masterController, ActionSelector actionSelector) {
        this.masterController = masterController;
        this.actionSelector = actionSelector;
    }

    @Override
    public void showActions() {
        for (MasterAction action : MasterAction.values()) {
            System.out.println("Enter [" + action.getName() + "] to perform.");
        }
    }

    @Override
    public Action chooseValidAction() {
        String input = this.actionSelector.getInput();
        return MasterAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        MasterAction masterAction = (MasterAction) action;
        switch (masterAction) {
            case CHANGE_STATUS:
                changeStatus();
                break;
            case LOGOUT:
                this.masterController.logout();
                break;
            case EXIT:
                return false;
        }
        return true;
    }

    private void changeStatus() {
        List<Ticket> tickets = this.masterController.getTicketsByEmail(CurrentSession.getSession().getEmail());
        this.actionSelector.showTickets(tickets);

        Long ticketId = this.actionSelector.getTicketId();
        System.out.printf("Valid options=%s%n", List.of(TicketStatus.IN_PROGRESS, TicketStatus.DONE));
        String status = this.actionSelector.getStatus();

        this.masterController.updateStatus(ticketId, status);
    }

}
