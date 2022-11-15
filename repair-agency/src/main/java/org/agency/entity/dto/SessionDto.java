package org.agency.entity.dto;

import java.time.LocalTime;

public class SessionDto {

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
