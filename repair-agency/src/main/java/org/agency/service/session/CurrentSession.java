package org.agency.service.session;

import org.agency.entity.Role;

public class CurrentSession {

    private static final Session session = new Session();

    public static Session getSession() {
        return session;
    }

    public static void setRole(Role role) {
        session.setRole(role);
    }

    public static Role getRole() {
        return session.getRole();
    }

    public static void clear() {
        session.clear();
    }

}
