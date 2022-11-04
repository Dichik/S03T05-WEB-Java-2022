package org.agency.controller;

import java.math.BigDecimal;

public class ManagerController {

    public ManagerController() {

    }

    public void addMasterToTicket(Long masterId, Long ticketId) {
        // TODO implement addMasterToTicket() method
    }

    public BigDecimal calculateTicketCost(Long ticketId) {
        // TODO here we should calculate how much it costs to master to fix ticket
        return null;
    }

    public void setStatus(String status) { // TODO create enum for statuses
        if (validateStatusChange(status, status)) {
            return;
        }
    }

    private boolean validateStatusChange(String oldStatus, String newStatus) {
        // TODO fix this method
        return oldStatus.equals(newStatus) || (oldStatus.equals("new") && newStatus.equals("in_progress"));
    }



}
