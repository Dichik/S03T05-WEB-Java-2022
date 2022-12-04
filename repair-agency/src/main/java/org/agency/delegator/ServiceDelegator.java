package org.agency.delegator;

import org.agency.service.BaseService;
import org.agency.service.auth.AuthService;
import org.agency.service.feedback.FeedbackService;
import org.agency.service.manager.ManagerService;
import org.agency.service.master.MasterService;
import org.agency.service.ticket.TicketService;
import org.agency.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

public class ServiceDelegator implements Delegator<BaseService> {

    private final Map<Class<?>, BaseService> services;

    public ServiceDelegator(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.services = new HashMap<>() {{
            put(AuthService.class, new AuthService(repositoryDelegator));
            put(TicketService.class, new TicketService(repositoryDelegator));
            put(MasterService.class, new MasterService(repositoryDelegator));
            put(UserService.class, new UserService(repositoryDelegator));
            put(ManagerService.class, new ManagerService(repositoryDelegator));
            put(FeedbackService.class, new FeedbackService(repositoryDelegator));
        }};
    }

    @Override
    public BaseService getByClass(Class<?> clazz) throws ClassNotFoundException {
        if (!this.services.containsKey(clazz)) {
            throw new ClassNotFoundException("There is no class " + clazz);
        }
        return this.services.getOrDefault(clazz, null);
    }

}
