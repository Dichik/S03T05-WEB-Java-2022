package org.agency.service.operation.performer.action;

public enum UserAction implements Action {
    SUBMIT_TICKET,
    //    LEAVE_FEEDBACK,
    SHOW_MY_TICKETS,
    SHOW_BALANCE,
    LOGOUT,
    EXIT;

    // TODO add action to check balance
    // TODO to add action to pay for ticket (should we be able to create next ticket if we have unpayed?)

    @Override
    public String getName() {
        return this.toString();
    }

}
