package org.agency.service.operation;

import org.agency.entity.Role;

public class OperationsService {

    private final OperationsInterface operator;

    public OperationsService(Role role) throws Exception {
        if (role == Role.MANAGER) {
            operator = new ManagerOperations();
        } else if (role == Role.MASTER) {
            operator = new MasterOperations();
        } else if (role == Role.USER) {
            operator = new UserOperations();
        } else {
            throw new Exception("Wrong role...");
        }
    }

    public void perform() {
//        operator.perform(action);

    }

}
