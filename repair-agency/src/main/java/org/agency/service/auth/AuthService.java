package org.agency.service.auth;

import org.agency.entity.Manager;
import org.agency.entity.Master;
import org.agency.entity.Role;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.WrongPasswordOnLoginException;
import org.agency.repository.manager.ManagerRepository;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO encrypt password
 */
@Service
public class AuthService implements BaseService {
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public AuthService(UserRepository userRepository, MasterRepository masterRepository, ManagerRepository managerRepository) throws ClassNotFoundException {
        this.userRepository = userRepository;
        this.masterRepository = masterRepository;
        this.managerRepository = managerRepository;
    }

    public void register(String email, String password, Role role) {
        switch (role) {
            case MASTER:
                if (this.masterRepository.existsByEmail(email)) {
                    logger.error(String.format("Master with email=[%s] already exists.", email));
                    return;
                }
                Master master = Master.builder()
                        .email(email)
                        .password(password)
                        .build();
                this.masterRepository.save(master);
                break;
            case MANAGER:
                if (this.managerRepository.existsByEmail(email)) {
                    logger.error(String.format("Manager with email=[%s] already exists.", email));
                    return;
                }
                Manager manager = Manager.builder()
                        .email(email)
                        .password(password)
                        .build();
                this.managerRepository.save(manager);
                break;
            case USER:
                if (this.userRepository.existsByEmail(email)) {
                    logger.error(String.format("User with email=[%s] already exists.", email));
                    return;
                }
                User user = User.builder()
                        .email(email)
                        .password(password)
                        .build();
                this.userRepository.save(user);
                break;
            default:
                logger.warn("Registration was not successful, because specified role is not correct.");
                return;
        }
        logger.info("Registration was successful.");
    }

    public void login(String email, String password, Role role) throws EntityNotFoundException, WrongPasswordOnLoginException {

    }

    public void logout() {
        CurrentSession.clear();
        logger.info("Logout was successfully performed.");
    }

}
