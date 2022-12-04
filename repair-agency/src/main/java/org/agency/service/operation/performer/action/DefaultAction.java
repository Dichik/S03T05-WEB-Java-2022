package org.agency.service.operation.performer.action;

public enum DefaultAction implements Action {
    LOGIN,
    REGISTER,
    EXIT;

    @Override
    public String getName() {
        return this.toString();
    }

}
