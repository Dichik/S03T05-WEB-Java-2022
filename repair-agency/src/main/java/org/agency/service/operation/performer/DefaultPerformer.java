package org.agency.service.operation.performer;

import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Role;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.WrongPasswordOnLoginException;
import org.agency.service.auth.AuthService;
import org.agency.service.operation.ActionPerformer;
import org.agency.service.operation.performer.action.Action;
import org.agency.service.operation.performer.action.DefaultAction;
import org.agency.view.ActionSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultPerformer implements ActionPerformer {
    private static final Logger logger = LogManager.getLogger(DefaultPerformer.class);

    private final AuthService authService;
    private final ActionSelector actionSelector;

    public DefaultPerformer(ServiceDelegator serviceDelegator, ActionSelector actionSelector) {
        this.authService = (AuthService) serviceDelegator.getByClass(AuthService.class);
        this.actionSelector = actionSelector;
    }

    @Override
    public void showActions() {
        for (DefaultAction action : DefaultAction.values()) {
            System.out.println("Enter [" + action.getName() + "] to perform.");
        }
    }

    @Override
    public Action chooseValidAction() {
        String input = this.actionSelector.getInput();
        return DefaultAction.valueOf(input.toUpperCase());
    }

    @Override
    public boolean performAction(Action action) {
        DefaultAction defaultAction = (DefaultAction) action;
        switch (defaultAction) {
            case LOGIN:
                login();
                break;
            case REGISTER:
                register();
                break;
            default:
                return defaultAction != DefaultAction.EXIT;
        }
        return true;
    }

    private void login() {
        Role role = this.actionSelector.getRole();
        String email = this.actionSelector.getEmail(ActionSelector.ENTER_EMAIL);
        String password = this.actionSelector.getPassword();

        try {
            this.authService.login(email, password, role);
        } catch (EntityNotFoundException e) {
            logger.error("Entity was not found, see: " + e);
        } catch (WrongPasswordOnLoginException e) {
            logger.error("Wrong password, see: " + e);
        }
    }

    private void register() {
        Role role = this.actionSelector.getRole();
        String email = this.actionSelector.getEmail(ActionSelector.ENTER_EMAIL);
        String password = this.actionSelector.getPassword();
        this.actionSelector.getSamePassword(password);

        this.authService.register(email, password, role);
    }

}
