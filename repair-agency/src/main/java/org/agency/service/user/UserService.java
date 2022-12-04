package org.agency.service.user;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.Ticket;
import org.agency.entity.User;
import org.agency.exception.EntityNotFoundException;
import org.agency.repository.ticket.TicketRepository;
import org.agency.repository.user.UserRepository;
import org.agency.service.BaseService;

import java.math.BigDecimal;
import java.util.Optional;

public class UserService implements BaseService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public UserService(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.userRepository = (UserRepository) repositoryDelegator.getByClass(UserRepository.class);
        this.ticketRepository = (TicketRepository) repositoryDelegator.getByClass(TicketRepository.class);
    }

    public void topUpBalance(String email, BigDecimal amount) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with " + email + " email was not found"));
        user.topUp(amount);
        this.userRepository.update(user.getId(), user);
    }

    public Optional<User> findByEmail(String userEmail) throws EntityNotFoundException {
        return this.userRepository.findByEmail(userEmail);
    }

    public void payForTicket(Long ticketId, String userEmail) throws EntityNotFoundException {
        User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with " + userEmail + " email was not found"));

        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with " + ticketId + " id was not found"));

        user.drawback(ticket.getPrice());
        this.userRepository.update(user.getId(), user);
    }

}
