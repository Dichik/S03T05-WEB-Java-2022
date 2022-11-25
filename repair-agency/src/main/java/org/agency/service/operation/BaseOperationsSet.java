package org.agency.service.operation;

import org.agency.service.operation.performer.action.Action;

public interface BaseOperationsSet {

    void showActionsList();

    Action chooseAction();

    void performAction(Action action);

}
