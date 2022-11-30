package org.agency.service.operation;

import org.agency.service.operation.performer.action.Action;

public interface ActionPerformer {

    void showActions();

    Action chooseValidAction();

    boolean performAction(Action action);

}
