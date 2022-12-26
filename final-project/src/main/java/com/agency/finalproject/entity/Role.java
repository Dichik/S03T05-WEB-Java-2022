package com.agency.finalproject.entity;

public enum Role {
    NOT_AUTHORIZED,
    MANAGER,
    MASTER,
    USER;

    public String getName() {
        return this.toString();
    }

}
