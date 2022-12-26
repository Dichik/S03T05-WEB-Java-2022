package com.agency.finalproject.service.user;

import com.agency.finalproject.entity.Ticket;
import com.agency.finalproject.entity.User;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.repository.user.UserRepository;
import com.agency.finalproject.service.BaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService implements BaseService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public UserService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public void topUpBalance(String email, BigDecimal amount) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with " + email + " email was not found"));
        user.setBalance(amount.add(user.getBalance()));
        this.userRepository.save(user);
    }

    public Optional<User> findByEmail(String userEmail) throws EntityNotFoundException {
        return this.userRepository.findByEmail(userEmail);
    }

    public void payForTicket(Long ticketId, String userEmail) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with " + userEmail + " email was not found"));

        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " id was not found"));

        user.setBalance(user.getBalance().subtract(ticket.getPrice()));
        this.userRepository.save(user);
    }

}
