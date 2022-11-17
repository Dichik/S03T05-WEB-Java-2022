package org.agency.controller;

import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.BaseOperationsSet;

public class ActionController implements BaseOperationsSet {

    private ActionPerformer actionPerformer;

    public ActionController(ActionPerformer actionPerformer) {
        this.actionPerformer = actionPerformer;
    }

    @Override
    public void showActionsList() {
        this.actionPerformer.showActions();
    }

    @Override
    public void chooseAction() {
        // make validation here
    }

//    public boolean validateAction() {
//        return true;
//    }

    @Override
    public void performAction() {

    }

}
