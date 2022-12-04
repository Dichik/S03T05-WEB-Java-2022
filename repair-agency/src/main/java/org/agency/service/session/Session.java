package org.agency.service.session;

import org.agency.entity.Role;

public class Session {

    private String email;
    private Role role;
    private final Object lock;

    public Session() {
        this.role = Role.NOT_AUTHORIZED;
        lock = new Object();
    }

    public void setRole(Role role) {
        synchronized (lock) {
            this.role = role;
        }
    }

    public Role getRole() {
        synchronized (lock) {
            return this.role;
        }
    }

    public void setEmail(String email) {
        synchronized (lock) {
            this.email = email;
        }
    }

    public String getEmail() {
        synchronized (lock) {
            return this.email;
        }
    }

    public void clear() {
        synchronized (lock) {
            this.role = Role.NOT_AUTHORIZED;
        }
    }

}
