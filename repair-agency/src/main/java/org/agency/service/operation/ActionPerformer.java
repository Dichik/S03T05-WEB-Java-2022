package org.agency.service.operation;

public interface ActionPerformer {

    void showActions();

    String chooseValidAction();

    void performAction(String action);

}
