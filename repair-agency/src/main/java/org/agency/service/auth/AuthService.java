package org.agency.service.auth;

import org.agency.entity.Role;
import org.agency.entity.Session;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static Role currentRole;
    public Session session;
    public Map<String, String> users;

    // TODO we should have sync methods for auth

    public AuthService() {
        this.session = new Session();
        this.users = new HashMap<>();
    }

    public boolean register(String email, String password) {
        // TODO check if email registered
        // TODO encrypt password
        // TODO save to database
        this.users.put(email, password);
        return true;
    }

    public boolean login(String email, String password) {
        if (!this.users.containsKey(email)) {
            return false;
        }
        if (!this.users.getOrDefault(email, "").equals(password)) {
            return false;
        }
        this.session.setEmail(email);
        return true;
    }

    public void logout() {
        this.session.clear();
    }

    public boolean isAuthorised(String email) {
        return true;
    }

    public static Session getCurrentSession() {
        return null;
    }

    public void setAuthorisation(Role role) {
        this.currentRole = role;
    }

}
