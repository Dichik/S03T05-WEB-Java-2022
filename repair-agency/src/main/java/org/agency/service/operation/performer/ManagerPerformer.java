package org.agency.service.operation.performer;

import org.agency.controller.ManagerController;
import org.agency.entity.Ticket;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.ManagerAction;
import org.agency.view.ActionSelector;

import java.math.BigDecimal;
import java.util.List;

public class ManagerPerformer implements ActionPerformer {

    private final ManagerController managerController;
    private final ActionSelector actionSelector;

    public ManagerPerformer(ManagerController managerController, ActionSelector actionSelector) {
        this.managerController = managerController;
        this.actionSelector = actionSelector;
    }

    @Override
    public void showActions() {
        for (ManagerAction action : ManagerAction.values()) {
            System.out.println("Enter [" + action.getName() + "] to perform.");
        }
    }

    @Override
    public Action chooseValidAction() {
        String input = this.actionSelector.getInput();
        return ManagerAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        ManagerAction managerAction = (ManagerAction) action;
        switch (managerAction) {
            case ASSIGN_MASTER:
                assignMaster();
                break;
            case SET_PRICE:
                setPrice();
                break;
            case SHOW_TICKETS:
                showTickets(this.managerController.getTickets());
                break;
            case UPDATE_STATUS:
                updateStatus();
                break;
            case TOP_UP_BALANCE:
                topUpBalance();
                break;
            case SORT_BY_DATE:
                showTickets(this.managerController.getSortedByDate());
                break;
            case SORT_BY_PRICE:
                showTickets(this.managerController.getSortedByPrice());
                break;
            case SORT_BY_STATUS:
                showTickets(this.managerController.getSortedByStatus());
                break;
            case FILTER_BY_MASTER:
                filterByMaster();
                break;
            case FILTER_BY_STATUS:
                filterByStatus();
                break;
            case LOGOUT:
                this.managerController.logout();
                break;
            case EXIT:
                return false;
        }
        return true;
    }

    private void filterByStatus() {
        String status = this.actionSelector.getStatus();

        showTickets(this.managerController.getFilterByStatus(status));
    }

    private void filterByMaster() {
        String masterEmail = this.actionSelector.getEmail(ActionSelector.ENTER_MASTER_EMAIL);

        showTickets(this.managerController.getFilteredByMaster(masterEmail));
    }

    private void topUpBalance() {
        String email = this.actionSelector.getEmail(ActionSelector.ENTER_USER_EMAIL);
        double amount = this.actionSelector.getAmount();

        this.managerController.topUpAccount(email, new BigDecimal(amount));
    }

    private void updateStatus() {
        Long ticketId = this.actionSelector.getTicketId();
        String status = this.actionSelector.getStatus();

        this.managerController.setStatus(ticketId, status);
    }

    private void showTickets(List<Ticket> tickets) {
        this.actionSelector.showTickets(tickets);
    }

    private void setPrice() {
        Long tickedId = this.actionSelector.getTicketId();
        double price = this.actionSelector.getAmount();

        this.managerController.setTicketPrice(tickedId, new BigDecimal(price));
    }

    private void assignMaster() {
        Long ticketId = this.actionSelector.getTicketId();
        String masterEmail = this.actionSelector.getEmail(ActionSelector.ENTER_MASTER_EMAIL);

        this.managerController.assignMasterToTicket(ticketId, masterEmail);
    }
}
