package org.agency.controller;

import org.agency.service.operation.BaseOperationsSet;
import org.agency.service.operation.factory.PerformerFactory;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionController implements BaseOperationsSet {
    private static final Logger logger = LogManager.getLogger(ActionController.class);

    @Override
    public void showActionsList() {
        try {
            PerformerFactory.getByRole(CurrentSession.getRole()).showActions();
        } catch (Exception e) {
            logger.error("Can't show action list, see: " + e);
        }
    }

    @Override
    public Action chooseAction() {
        try {
            return PerformerFactory.getByRole(CurrentSession.getRole()).chooseValidAction();
        } catch (Exception e) {
            logger.error("Can't choose action, see: " + e);
            throw new RuntimeException();
        }
    }

    @Override
    public void performAction(Action action) {
        try {
            PerformerFactory.getByRole(CurrentSession.getRole()).performAction(action);
        } catch (Exception e) {
            logger.error("Can't choose action, see: " + e);
        }
    }

}
