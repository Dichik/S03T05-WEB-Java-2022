package org.agency.controller;

import org.agency.service.operation.delegator.PerformerDelegator;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionController {
    private static final Logger logger = LogManager.getLogger(ActionController.class);

    private final PerformerDelegator performerDelegator;

    public ActionController(PerformerDelegator performerDelegator) {
        this.performerDelegator = performerDelegator;
    }

    public void showActionsList() {
        try {
            performerDelegator.getByRole(CurrentSession.getRole()).showActions();
        } catch (Exception e) {
            logger.error("Can't show action list, see: " + e);
        }
    }

    public Action chooseAction() {
        try {
            return performerDelegator.getByRole(CurrentSession.getRole()).chooseValidAction();
        } catch (Exception e) {
            logger.error("Can't choose action, see: " + e);
            throw new RuntimeException();
        }
    }

    public void performAction(Action action) {
        try {
            performerDelegator.getByRole(CurrentSession.getRole()).performAction(action);
        } catch (Exception e) {
            logger.error("Can't choose action, see: " + e);
        }
    }

}
