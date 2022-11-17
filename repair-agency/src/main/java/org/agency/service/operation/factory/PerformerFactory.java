package org.agency.service.operation.factory;

import org.agency.entity.Role;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.ManagerPerformer;
import org.agency.service.operation.performer.MasterPerformer;
import org.agency.service.operation.performer.UserPerformer;

public class PerformerFactory {

    public PerformerFactory() {

    }

    public static ActionPerformer getCurrentPerformer(Role role) throws Exception {
        if (role == Role.MANAGER) {
            return new ManagerPerformer(); // FIXME add singleton
        } else if (role == Role.MASTER) {
            return new MasterPerformer(); // FIXME
        } else if (role == Role.USER) {
            return new UserPerformer();
        } else {
            throw new Exception();
        }
    }

}
