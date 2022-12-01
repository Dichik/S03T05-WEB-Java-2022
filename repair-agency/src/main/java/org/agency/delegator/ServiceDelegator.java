package org.agency.delegator;

import org.agency.service.BaseService;
import org.agency.service.auth.AuthService;
import org.agency.service.ticket.TicketService;

import java.util.HashMap;
import java.util.Map;

public class ServiceDelegator implements Delegator<BaseService> {

    private final Map<Class<?>, BaseService> services;

    public ServiceDelegator(RepositoryDelegator repositoryDelegator) {
        this.services = new HashMap<>(){{
           put(AuthService.class, new AuthService(repositoryDelegator));
           put(TicketService.class, new TicketService(repositoryDelegator));
        }};
    }

    @Override
    public boolean existsByClass(Class<?> clazz) {
        return this.services.containsKey(clazz);
    }

    @Override
    public BaseService getByClass(Class<?> clazz) {
        return this.services.getOrDefault(clazz, null);
    }

}
