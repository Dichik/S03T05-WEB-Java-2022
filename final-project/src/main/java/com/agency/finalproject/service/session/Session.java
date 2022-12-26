package com.agency.finalproject.service.session;


import com.agency.finalproject.entity.Role;

public class Session {

    private final Object lock;
    private String email;
    private Role role;

    public Session() {
        this.role = Role.NOT_AUTHORIZED;
        lock = new Object();
    }

    public Role getRole() {
        synchronized (lock) {
            return this.role;
        }
    }

    public void setRole(Role role) {
        synchronized (lock) {
            this.role = role;
        }
    }

    public String getEmail() {
        synchronized (lock) {
            return this.email;
        }
    }

    public void setEmail(String email) {
        synchronized (lock) {
            this.email = email;
        }
    }

    public void clear() {
        synchronized (lock) {
            this.role = Role.NOT_AUTHORIZED;
        }
    }

}
