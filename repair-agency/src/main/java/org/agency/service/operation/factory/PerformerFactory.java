package org.agency.service.operation.factory;

import org.agency.entity.Role;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.DefaultPerformer;
import org.agency.service.operation.performer.ManagerPerformer;
import org.agency.service.operation.performer.MasterPerformer;
import org.agency.service.operation.performer.UserPerformer;

import java.util.HashMap;
import java.util.Map;

public class PerformerFactory {

    private static final Map<Role, ActionPerformer> performers = new HashMap<>() {{
        put(Role.NOT_AUTHORIZED, new DefaultPerformer());
        put(Role.MANAGER, new ManagerPerformer());
        put(Role.MASTER, new MasterPerformer());
        put(Role.USER, new UserPerformer());
    }};

    public static ActionPerformer getByRole(Role role) throws Exception {
        if (!performers.containsKey(role)) {
            throw new Exception();
        }
        return performers.get(role);
    }

}
