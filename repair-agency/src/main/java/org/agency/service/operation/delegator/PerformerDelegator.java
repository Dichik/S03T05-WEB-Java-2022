package org.agency.service.operation.delegator;

import org.agency.controller.ManagerController;
import org.agency.controller.MasterController;
import org.agency.controller.UserController;
import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Role;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.DefaultPerformer;
import org.agency.service.operation.performer.ManagerPerformer;
import org.agency.service.operation.performer.MasterPerformer;
import org.agency.service.operation.performer.UserPerformer;
import org.agency.view.ActionSelector;

import java.util.HashMap;
import java.util.Map;

public class PerformerDelegator {

    private final Map<Role, ActionPerformer> performers;

    public PerformerDelegator(ServiceDelegator serviceDelegator, ActionSelector actionSelector) {
        this.performers = new HashMap<>() {{
            put(Role.NOT_AUTHORIZED, new DefaultPerformer(serviceDelegator, actionSelector));
            put(Role.MANAGER, new ManagerPerformer(new ManagerController(serviceDelegator), actionSelector));
            put(Role.MASTER, new MasterPerformer(new MasterController(serviceDelegator), actionSelector));
            put(Role.USER, new UserPerformer(new UserController(serviceDelegator), actionSelector));
        }};
    }

    public ActionPerformer getByRole(Role role) throws Exception {
        if (!this.performers.containsKey(role)) {
            throw new Exception();
        }
        return this.performers.get(role);
    }

}
