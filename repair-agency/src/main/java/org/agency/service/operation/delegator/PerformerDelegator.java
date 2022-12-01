package org.agency.service.operation.delegator;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Role;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.DefaultPerformer;
import org.agency.service.operation.performer.ManagerPerformer;
import org.agency.service.operation.performer.MasterPerformer;
import org.agency.service.operation.performer.UserPerformer;

import java.util.HashMap;
import java.util.Map;

public class PerformerDelegator {

    private final Map<Role, ActionPerformer> performers;

    public PerformerDelegator(ServiceDelegator serviceDelegator) {
        this.performers = new HashMap<>() {{
            put(Role.NOT_AUTHORIZED, new DefaultPerformer(serviceDelegator));
            put(Role.MANAGER, new ManagerPerformer());
            put(Role.MASTER, new MasterPerformer(serviceDelegator));
            put(Role.USER, new UserPerformer(serviceDelegator));
        }};
    }

    public ActionPerformer getByRole(Role role) throws Exception {
        if (!this.performers.containsKey(role)) {
            throw new Exception();
        }
        return this.performers.get(role);
    }

}
