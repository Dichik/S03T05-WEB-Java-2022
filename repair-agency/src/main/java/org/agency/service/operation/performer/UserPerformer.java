package org.agency.service.operation.performer;

import org.agency.controller.UserController;
import org.agency.entity.Ticket;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.UserAction;
import org.agency.service.session.CurrentSession;
import org.agency.view.ActionSelector;

import java.math.BigDecimal;
import java.util.List;

public class UserPerformer implements ActionPerformer {

    private final UserController userController;
    private final ActionSelector actionSelector;

    public UserPerformer(UserController userController, ActionSelector actionSelector) {
        this.userController = userController;
        this.actionSelector = actionSelector;
    }

    @Override
    public void showActions() {
        for (UserAction action : UserAction.values()) {
            System.out.println("Enter [" + action.getName() + "] to perform.");
        }
    }

    @Override
    public Action chooseValidAction() {
        String input = this.actionSelector.getInput();
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
        switch (userAction) {
            case SUBMIT_TICKET:
                submitTicket();
                break;
            case SHOW_MY_TICKETS:
                showMyTickets();
                break;
            case SHOW_BALANCE:
                showMyBalance();
                break;
            case LEAVE_FEEDBACK:
                leaveFeedback();
                break;
            case LOGOUT:
                this.userController.logout();
                break;
            default:
                return userAction != UserAction.EXIT;
        }
        return true;
    }

    private void leaveFeedback() {
        Long ticketId = this.actionSelector.getTicketId();
        String feedback = this.actionSelector.getInput(ActionSelector.ENTER_FEEDBACK);

        this.userController.leaveFeedback(ticketId, feedback);
    }

    private void showMyBalance() {
        BigDecimal balance = this.userController.getCurrentBalance();
        this.actionSelector.showBalance(balance);
    }

    private void showMyTickets() {
        List<Ticket> tickets = this.userController.getTicketsByUserEmail(CurrentSession.getSession().getEmail());
        this.actionSelector.showTickets(tickets);
    }

    private void submitTicket() {
        String title = this.actionSelector.getInput(ActionSelector.ENTER_TITLE);
        String description = this.actionSelector.getInput(ActionSelector.ENTER_DESCRIPTION);

        Ticket ticket = new Ticket.TicketBuilder(title, description,
                CurrentSession.getSession().getEmail()).build();
        this.userController.createTicket(ticket);
    }

}
