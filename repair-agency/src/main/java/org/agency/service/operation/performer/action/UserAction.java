package org.agency.service.operation.performer.action;

public enum UserAction implements Action {
    SUBMIT_TICKET,
    PAY_FOR_TICKET,
    LEAVE_FEEDBACK,
    SHOW_MY_TICKETS,
    SHOW_BALANCE,
    LOGOUT,
    EXIT;

    @Override
    public String getName() {
        return this.toString();
    }

}
