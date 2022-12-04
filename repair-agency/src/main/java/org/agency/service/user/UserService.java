package org.agency.service.user;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.user.UserRepository;
import org.agency.service.BaseService;

import java.math.BigDecimal;

public class UserService implements BaseService {

    private final UserRepository userRepository;

    public UserService(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.userRepository = (UserRepository) repositoryDelegator.getByClass(UserRepository.class);
    }

    public boolean leaveFeedback(Long ticketId) {
        // TODO should be mapped from dto_id to entity_id
        return false;
    }

    public void topUpBalance(String email, BigDecimal amount) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User with " + email + " email was not found");
        }
        user.topUp(amount);
        this.userRepository.update(user.getId(), user);
    }

    public User findByEmail(String userEmail) throws EntityNotFoundException {
        return this.userRepository.findByEmail(userEmail);
    }

}
