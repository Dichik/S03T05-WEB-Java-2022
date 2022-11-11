package org.agency.service.user;

import org.agency.entity.Ticket;
import org.agency.entity.User;
import org.agency.repository.user.UserRepository;

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

    public void update(User user) {
        this.userRepository.update(user);
    }

    public User findByEmail(String userEmail) {
        return this.userRepository.findByEmail(userEmail);
    }
}
