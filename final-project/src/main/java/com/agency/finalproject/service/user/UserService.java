package com.agency.finalproject.service.user;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.NotEnoughMoneyException;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public UserService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public User topUpBalance(String username, BigDecimal amount) throws EntityNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with " + username + " username was not found"));
        user.setBalance(amount.add(user.getBalance()));
        return this.userRepository.save(user);
    }

    public User payForTicket(Long ticketId, String userEmail) throws EntityNotFoundException, ItemWasNotFoundException, NotEnoughMoneyException {
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with " + userEmail + " email was not found"));

        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " id was not found"));

        if (!isUserRole(user)) {
            throw new ItemWasNotFoundException("This type of operation is supported only for users.");
        }

        if (user.getBalance().compareTo(ticket.getPrice()) < 0) {
            throw new NotEnoughMoneyException("Please top up your user account.");
        }

        user.setBalance(user.getBalance().subtract(ticket.getPrice()));
        return this.userRepository.save(user);
    }

    private boolean isUserRole(User user) {
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getName().equals(ERole.ROLE_USER)) {
                return true;
            }
        }
        return false;
    }

    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }
}
