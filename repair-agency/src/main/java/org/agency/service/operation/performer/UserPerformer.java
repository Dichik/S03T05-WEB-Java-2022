package org.agency.service.operation.performer;

import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(UserPerformer.class);

    @Override
    public void showActions() {
        logger.info("User performer [show actions] is waiting to be implemented...");
    }

    @Override
    public Action chooseValidAction() {
        logger.info("User performer [choose action] is waiting to be implemented...");
        return null;
    }

    @Override
    public boolean performAction(Action action) {
        logger.info("User performer [perform action] is waiting to be implemented...");
        return true;
    }
}
