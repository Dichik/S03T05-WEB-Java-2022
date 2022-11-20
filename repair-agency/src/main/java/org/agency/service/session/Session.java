package org.agency.service.session;

import org.agency.entity.Role;

import java.sql.Timestamp;

public class Session {

    private Role role;
    private Timestamp authorizedAt;
    private final Object lock;

    public Session() {
        this.role = Role.NOT_AUTHORIZED;
        lock = new Object();
    }

    public void setRole(Role role) {
        synchronized (lock) {
            this.role = role;
            this.authorizedAt = new Timestamp(System.currentTimeMillis());
        }
    }

    public Role getRole() {
        synchronized (lock) {
            return this.role;
        }
    }

    public Timestamp getAuthorizedTime() {
        synchronized (lock) {
            return this.authorizedAt; // FIXME should we return exception if not authorized?
        }
    }

    public void clear() {
        synchronized (lock) {
            this.role = Role.NOT_AUTHORIZED;
            this.authorizedAt = null;
        }
    }

}
