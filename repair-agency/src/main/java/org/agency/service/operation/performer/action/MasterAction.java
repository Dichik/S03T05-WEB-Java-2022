package org.agency.service.operation.performer.action;

public enum MasterAction implements Action {
    CHANGE_STATUS,
    LOGOUT,
    EXIT;

    @Override
    public String getName() {
        return this.toString();
    }

}
