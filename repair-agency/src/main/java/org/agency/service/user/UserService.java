package org.agency.service.user;

import org.agency.entity.Ticket;
import org.agency.entity.User;
import org.agency.exception.UserNotFoundException;
import org.agency.repository.user.UserRepository;

import java.math.BigDecimal;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO add annotatio
    public List<Ticket> getActiveTickets() {
        // TODO implement this method
        return null;
    }

    public List<Ticket> getDoneTickets() {
        // TODO implement this method
        return null;
    }

    public boolean leaveFeedback(Long ticketId) {

        // TODO should be mapped from dto_id to entity_id

        return false;
    }

    public void topUpBalance(String email, BigDecimal amount) throws UserNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with " + email + " email was not found");
        }
        BigDecimal currentBalance = user.topUp(amount);
        this.userRepository.update(user);
    }

    public User findByEmail(String userEmail) {
        return this.userRepository.findByEmail(userEmail);
    }
}
