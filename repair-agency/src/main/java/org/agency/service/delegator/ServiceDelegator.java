package org.agency.service.delegator;

import org.agency.repository.delegator.RepositoryDelegator;
import org.agency.service.BaseService;
import org.agency.service.auth.AuthService;

import java.util.HashMap;
import java.util.Map;

public class ServiceDelegator {

    private final Map<Class, BaseService> services;

    public ServiceDelegator(RepositoryDelegator repositoryDelegator) {
        this.services = new HashMap<>(){{
           put(AuthService.class, new AuthService(repositoryDelegator));
        }};
    }

    public BaseService getByClass(Class<?> clazz) {
        return this.services.getOrDefault(clazz, null);
    }

}
