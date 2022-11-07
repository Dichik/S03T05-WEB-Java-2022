package org.agency.entity;

public enum Role {
    MANAGER(1),
    MASTER(2),
    USER(3);

    private int index;

    Role(int index) {
        this.index = index;
    }

    public static Role getRoleByIndex(int index) {
        for (Role role : Role.values()) {
            if (role.getIndex() == index) {
                return role;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

}
