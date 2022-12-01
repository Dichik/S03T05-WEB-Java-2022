package org.agency.entity;

public enum Role {
    NOT_AUTHORIZED,
    MANAGER,
    MASTER,
    USER;

    public String getName() {
        return this.toString();
    }

}
