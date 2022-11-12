package org.agency.repository.ticket.filter;

public class CustomTicketFilter {

    private String master;
    private String status;

    public boolean isFilterableByMaster() {
        return false;
    }

    public boolean isFilterableByStatus() {
        return false;
    }

}
