package org.agency.service.operation.performer.action;

public enum DefaultAction implements Action {
    LOGIN("login"),
    REGISTER("register"),
    EXIT("exit");

    private final String name;

    DefaultAction(String name) {
        this.name = name;
    }

    public static Action getByName(String name) {
        for (DefaultAction action: DefaultAction.values()) {
            if (name.equals(action.getName())) {
                return action;
            }
        }
        throw new RuntimeException("Error...");
    }

    @Override
    public String getName() {
        return this.name;
    }

}
