package com.agency.finalproject.service.user;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.NotEnoughMoneyException;
import com.agency.finalproject.repository.role.RoleRepository;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    private User USER;
    private Ticket TICKET;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        Role role = this.roleRepository.findByName(ERole.ROLE_USER).orElse(null);
        assertNotNull(role, "Role was not found.");

        Set<Role> roles = Set.of(role);
        long l = System.currentTimeMillis();
        this.USER = this.userRepository.save(User.builder()
                .username("user" + l)
                .password("password")
                .email("user" + l + "@user.com")
                .balance(BigDecimal.ZERO)
                .roles(roles)
                .build());
        assertNotNull(this.USER, "Default user was not created.");


        Ticket ticket = Ticket.builder()
                .title("Ticket #1")
                .status(TicketStatus.DONE)
                .description("keyboard was broken...")
                .build();
        this.TICKET = this.ticketRepository.save(ticket);
        assertNotNull(this.TICKET, "Couldn't save ticket to database.");
    }

    @Test
    void topUpBalance() {
    }

    @Test
    void payForTicket() throws NotEnoughMoneyException, ItemWasNotFoundException {
        this.TICKET.setPrice(BigDecimal.ONE);
        Ticket ticket = this.ticketRepository.save(this.TICKET);
        assertNotNull(ticket, "Couldn't update ticket.");

        this.USER.setBalance(BigDecimal.TEN);
        User user = this.userRepository.save(this.USER);
        assertNotNull(user, "Couldn't update user.");

        BigDecimal expectedBalance = BigDecimal.TEN.subtract(BigDecimal.ONE);
        this.userService.payForTicket(this.TICKET.getId(), this.USER.getEmail());
        user = this.userRepository.findById(this.USER.getId()).orElse(null);
        assertNotNull(user, "User was not found.");
        assertEquals(0, user.getBalance().compareTo(expectedBalance), "Pay for ticket doesn't work.");
    }

    @Test
    void payForTicketWithInvalidEmail() {
        String invalidEmail = this.USER.getEmail() + "$invalid_suffix$";
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            this.userService.payForTicket(this.TICKET.getId(), invalidEmail);
        });

        String expectedMessage = "User with " + invalidEmail + " email was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void payForTicketWithInvalidId() {
        Long invalidId = -1L;
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            this.userService.payForTicket(invalidId, this.USER.getEmail());
        });

        String expectedMessage = "Ticket with " + invalidId + " id was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void payForTicketWithUnsupportedRole() {
        this.USER.setRoles(Set.of());
        User user = this.userRepository.save(this.USER);
        assertNotNull(user, "Couldn't update user.");
        assertEquals(this.USER.getRoles(), Set.of(), "Roles must be empty!");

        Exception exception = assertThrows(ItemWasNotFoundException.class, () -> {
            this.userService.payForTicket(this.TICKET.getId(), this.USER.getEmail());
        });

        String expectedMessage = "This type of operation is supported only for users.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void payForTicketWithNotEnoughMoney() {
        this.TICKET.setPrice(BigDecimal.TEN);
        this.ticketRepository.save(this.TICKET);

        assertTrue(this.TICKET.getPrice().compareTo(this.USER.getBalance()) > 0, "Ticket must have more cost.");

        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            this.userService.payForTicket(this.TICKET.getId(), this.USER.getEmail());
        });

        String expectedMessage = "Please top up your user account.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        this.TICKET.setPrice(BigDecimal.ZERO);
    }

    @Test
    void findById() {
        Long id = this.USER.getId();
        User user = this.userRepository.findById(id).orElse(null);
        assertNotNull(user, "User was not found.");
    }
}