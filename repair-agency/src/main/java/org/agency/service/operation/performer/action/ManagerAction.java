package org.agency.service.operation.performer.action;

public enum ManagerAction implements Action {
    ASSIGN_MASTER,
    SET_PRICE,
    SHOW_TICKETS,
    UPDATE_STATUS,
    TOP_UP_BALANCE,
    SORT_BY_DATE,
    SORT_BY_STATUS,
    SORT_BY_PRICE,
    FILTER_BY_STATUS,
    FILTER_BY_MASTER;

    @Override
    public String getName() {
        return this.toString();
    }

}
