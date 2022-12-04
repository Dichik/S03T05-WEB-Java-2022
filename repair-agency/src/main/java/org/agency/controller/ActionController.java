package org.agency.controller;

import org.agency.exception.InvalidActionException;
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
            this.performerDelegator.getByRole(CurrentSession.getRole()).showActions();
        } catch (Exception e) {
            logger.error("Can't show action list, see: " + e);
        }
    }

    public Action chooseAction() throws InvalidActionException {
        try {
            return this.performerDelegator.getByRole(CurrentSession.getRole()).chooseValidAction();
        } catch (IllegalArgumentException e) {
            String message = "Error while choosing action occurred. See: " + e;
            throw new InvalidActionException(message);
        } catch (Exception e) {
            String message = "Couldn't choose action, see: " + e;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    public boolean performAction(Action action) {
        try {
            return this.performerDelegator.getByRole(CurrentSession.getRole()).performAction(action);
        } catch (Exception e) {
            logger.error("Can't perform action, see: " + e);
            return true;
        }
    }

}
