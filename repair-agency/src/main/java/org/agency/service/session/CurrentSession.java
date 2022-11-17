package org.agency.service.session;

import org.agency.entity.Role;

public class CurrentSession {

    public static Session getSession() {
        // TODO should have generic thing to get sessions
        // TODO find appropriate design pattern
        return new Session();
    }

    public static Role getCurrentRole() {
        // TODO implement the method
        return Role.MASTER;
    }
}
