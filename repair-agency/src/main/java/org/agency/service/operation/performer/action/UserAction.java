package org.agency.service.operation.performer.action;

public enum UserAction implements Action {
    SUBMIT_TICKET,
    //    LEAVE_FEEDBACK,
    SHOW_MY_TICKETS,
    LOGOUT,
    EXIT;

    @Override
    public String getName() {
        return this.toString();
    }

//    @Override
//    public UserAction valueOf(String name) {
//
//    }

}
