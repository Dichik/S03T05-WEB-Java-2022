package org.agency.service.auth;

import org.agency.entity.Role;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.exception.UserAlreadyRegisteredException;
import org.agency.repository.PersonRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.session.CurrentSession;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String email, String password) throws UserAlreadyRegisteredException, EntityNotFoundException {
        // TODO encrypt password
        if (this.userRepository.findByEmail(email) != null) {
            throw new UserAlreadyRegisteredException("Email=[" + email + "] is already registered!");
        }
        User user = new User.UserBuilder(email)
                .setPassword(password)
                .build();
        this.userRepository.create(user);
        return true;
    }

    public boolean login(String email, String password, PersonRepository<?> repository) throws EntityNotFoundException {
        Object object = repository.findByEmail(email);
        if (object == null) {
            return false;
        }
        // setRole
        // FIXME how to login using personRepository???
        return true;
    }

    public void logout() {
        CurrentSession.clear();
    }

    public boolean isAuthorised() {
        return (CurrentSession.getRole() != Role.NOT_AUTHORIZED);
    }

    public void authorize(Role role) {
        CurrentSession.setRole(role);
    }

}
