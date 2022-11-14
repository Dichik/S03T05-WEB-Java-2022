package org.agency.entity;

public enum TicketStatus {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    DONE("done"),
    WAITING_FOR_PAYMENT("waiting_for_payment"),
    PAID("paid"),
    CANCELED("canceled");

    private final String status;

    TicketStatus(String status) {
        this.status = status;
    }

    public static TicketStatus getTicketStatusByName(String statusName) {
        for (TicketStatus status : TicketStatus.values()) {
            if (status.getStatus().equals(statusName)) {
                return status;
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

}
