package org.agency.service.session;

import org.agency.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CurrentSession {
    private static final Logger logger = LogManager.getLogger(CurrentSession.class);

    private static final Session session = new Session();

    public static Session getSession() {
        return session;
    }

    public static void setRole(String email, Role role) {
        session.setRole(role);
        session.setEmail(email);
        logger.info("Authorized as " + role + ". Any further operations will be processed by this role.");
    }

    public static Role getRole() {
        return session.getRole();
    }

    public static void clear() {
        session.clear();
        logger.info("Session was cleared. Role is set to " + Role.NOT_AUTHORIZED);
    }

}
