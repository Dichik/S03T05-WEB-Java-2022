package org.agency.service.operation.performer;

import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;

public class MasterPerformer implements ActionPerformer {
    @Override
    public void showActions() {

    }

    @Override
    public Action chooseValidAction() {
        return null;
    }

    @Override
    public boolean performAction(Action action) {
        return true;
    }
}
