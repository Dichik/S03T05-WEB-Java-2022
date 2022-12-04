package org.agency.service.auth;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.*;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.RoleNotFoundException;
import org.agency.exception.WrongPasswordOnLoginException;
import org.agency.repository.PersonRepository;
import org.agency.repository.manager.ManagerRepository;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * TODO encrypt password
 */
public class AuthService implements BaseService {
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final ManagerRepository managerRepository;

    public AuthService(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.userRepository = (UserRepository) repositoryDelegator.getByClass(UserRepository.class);
        this.masterRepository = (MasterRepository) repositoryDelegator.getByClass(MasterRepository.class);
        this.managerRepository = (ManagerRepository) repositoryDelegator.getByClass(ManagerRepository.class);
    }

    public void register(String email, String password, Role role) {
        switch (role) {
            case MASTER:
                if (this.masterRepository.existsByEmail(email)) {
                    logger.error(String.format("Master with email=[%s] already exists.", email));
                    return;
                }
                Master master = new Master.MasterBuilder(email, password).build();
                this.masterRepository.create(master);
                break;
            case MANAGER:
                if (this.managerRepository.existsByEmail(email)) {
                    logger.error(String.format("Manager with email=[%s] already exists.", email));
                    return;
                }
                Manager manager = new Manager.ManagerBuilder(email, password).build();
                this.managerRepository.create(manager);
                break;
            case USER:
                if (this.userRepository.existsByEmail(email)) {
                    logger.error(String.format("User with email=[%s] already exists.", email));
                    return;
                }
                User user = new User.UserBuilder(email).setPassword(password).build();
                this.userRepository.create(user);
                break;
            default:
                logger.warn("Registration was not successful, because specified role is not correct.");
                return;
        }
        logger.info("Registration was successful.");
    }

    public void login(String email, String password, Role role) throws EntityNotFoundException, WrongPasswordOnLoginException {
        PersonRepository<?> repository = getRepositoryByRole(role);
        Optional<?> obj = repository.findByEmail(email);
        if (!obj.isPresent()) {
            throw new EntityNotFoundException("Couldn't find person by email=[" + email + "]");
        }
        Person person = (Person) obj.get();
        if (!password.equals(person.getPassword())) {
            throw new WrongPasswordOnLoginException("Wrong password for email=[" + email + "]");
        }
        CurrentSession.setRole(email, role);
    }

    private PersonRepository<?> getRepositoryByRole(Role role) {
        switch (role) {
            case MASTER:
                return this.masterRepository;
            case MANAGER:
                return this.managerRepository;
            case USER:
                return this.userRepository;
            default:
                throw new NullPointerException("Repository for " + role + " doesn't exist.");
        }
    }

    public void logout() {
        CurrentSession.clear();
        logger.info("Logout was successfully performed.");
    }

}
