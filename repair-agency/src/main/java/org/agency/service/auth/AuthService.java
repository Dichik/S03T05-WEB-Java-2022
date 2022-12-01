package org.agency.service.auth;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.*;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.PersonRepository;
import org.agency.repository.manager.ManagerRepository;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;

public class AuthService implements BaseService {

    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final ManagerRepository managerRepository;

    public AuthService(RepositoryDelegator repositoryDelegator) {
//        if (!repositoryDelegator.existsByClass(User.class)) {
//            throw new RuntimeException("Error..."); // FIXME
//        }
        this.userRepository = (UserRepository) repositoryDelegator.getByClass(UserRepository.class);
        this.masterRepository = (MasterRepository) repositoryDelegator.getByClass(MasterRepository.class);
        this.managerRepository = (ManagerRepository) repositoryDelegator.getByClass(ManagerRepository.class);
    }

    public void register(String email, String password, Role role) {
        // TODO encrypt password
        // TODO already registered check
        if (role == Role.MASTER) {
            Master master = new Master();
            master.setEmail(email);
            master.setPassword(password);
            this.masterRepository.create(master);
        } else if (role == Role.MANAGER) {
            Manager manager = new Manager();
            manager.setEmail(email);
            manager.setPassword(password);
            this.managerRepository.create(manager);
        } else if (role == Role.USER) {
            User user = new User.UserBuilder(email)
                    .setPassword(password)
                    .build();
            this.userRepository.create(user);
        } else {
            throw new RuntimeException("Error...");
        }
    }

    public void login(String email, String password, Role role) throws EntityNotFoundException {
        PersonRepository<?> repository = getRepositoryByRole(role);
        Person person = (Person) repository.findByEmail(email);
        if (person == null) {
            throw new RuntimeException("Error..."); // FIXME
        }
        if (!password.equals(person.getPassword())) {
            throw new RuntimeException("Error..."); // FIXME
        }
        CurrentSession.setRole(email, role);
    }

    private PersonRepository<?> getRepositoryByRole(Role role) {
        if (role == Role.MASTER) {
            return this.masterRepository;
        } else if (role == Role.MANAGER) {
            return this.managerRepository;
        } else if (role == Role.USER) {
            return this.userRepository;
        } else {
            throw new RuntimeException("Error...");
        }
    }

    public void logout() {
        CurrentSession.clear();
    }

    public boolean isAuthorised() {
        return (CurrentSession.getRole() != Role.NOT_AUTHORIZED);
    }

}
