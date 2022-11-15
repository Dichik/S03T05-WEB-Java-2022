package org.agency.service.session;

public class CurrentSession {

    public static Session getSession() {
        // TODO should have generic thing to get sessions
        // TODO find appropriate design pattern
        return new Session();
    }

}
