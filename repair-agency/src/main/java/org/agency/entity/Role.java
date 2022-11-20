package org.agency.entity;

public enum Role {
    NOT_AUTHORIZED("not_authorized"),
    MANAGER("manager"),
    MASTER("master"),
    USER("user");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role getRoleByName(String name) {
        for (Role role : Role.values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

}
