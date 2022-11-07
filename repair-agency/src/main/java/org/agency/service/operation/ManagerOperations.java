package org.agency.service.operation;

import java.util.List;

public class ManagerOperations implements OperationsInterface {

    @Override
    public String getAvailableOperations() {
        StringBuilder sb = new StringBuilder();
        sb.append("1 - set master");
        sb.append("2 - see list of active tickets");
        sb.append("3 - change status of the ticket");
        return sb.toString();
    }

}
