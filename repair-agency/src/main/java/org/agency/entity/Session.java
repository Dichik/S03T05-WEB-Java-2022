package org.agency.entity;

import java.time.LocalTime;

public class Session {

    private String email;
    private LocalTime something;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void clear() {
        email = null;
    }

}
