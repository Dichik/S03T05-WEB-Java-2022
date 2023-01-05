package com.agency.finalproject.service.ticket;

import com.agency.finalproject.entity.role.ERole;
import com.agency.finalproject.entity.role.Role;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.entity.ticket.dto.TicketDto;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.exception.InvalidStatusChangeException;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.RoleLackOfPermissionException;
import com.agency.finalproject.exception.UnvalidStatusUpdateException;
import com.agency.finalproject.repository.ticket.TicketRepository;
import com.agency.finalproject.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TicketServiceTest {

    private static final String DEFAULT_USER_EMAIL = "user@user.com";
    private static final String DEFAULT_MASTER_EMAIL = "master@user.com";
    private Ticket TICKET;
    private List<Ticket> TICKETS;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @BeforeAll
    void createTickets() {
        this.TICKETS = new ArrayList<>();

        Ticket ticket = Ticket.builder()
                .title("Ticket #1")
                .status(TicketStatus.DONE)
                .userEmail(DEFAULT_USER_EMAIL)
                .description("keyboard was broken...")
                .build();
        this.TICKET = this.ticketService.createTicket(ticket);
        assertNotNull(this.TICKET, "Couldn't create ticket to database.");
        this.TICKETS.add(this.TICKET);

        Ticket ticket2 = Ticket.builder()
                .title("Ticket #2")
                .status(TicketStatus.NEW)
                .price(BigDecimal.TEN)
                .masterEmail(DEFAULT_MASTER_EMAIL)
                .description("keyboard was broken...")
                .build();
        this.TICKET = this.ticketService.createTicket(ticket2);
        assertNotNull(this.TICKET, "Couldn't create ticket to database.");
        this.TICKETS.add(this.TICKET);
    }

    @Test
    void getTicketsByUserEmail() {
        List<Ticket> tickets = this.ticketService.getTicketsByUserEmail(DEFAULT_USER_EMAIL);
        assertEquals(tickets.size(), 1, "Didn't find ticket with email=" + DEFAULT_USER_EMAIL);

        tickets = this.ticketService.getTicketsByUserEmail(DEFAULT_USER_EMAIL + "$invalid_suffix$");
        assertEquals(tickets.size(), 0, "Found extra ticket with email=" + DEFAULT_USER_EMAIL);
    }

    @Test
    void getTicketsByMasterEmail() {
        List<Ticket> tickets = this.ticketService.getTicketsByMasterEmail(DEFAULT_MASTER_EMAIL);
        assertEquals(tickets.size(), 1, "Didn't find ticket with email=" + DEFAULT_MASTER_EMAIL);

        tickets = this.ticketService.getTicketsByMasterEmail(DEFAULT_MASTER_EMAIL + "$invalid_suffix$");
        assertEquals(tickets.size(), 0, "Found extra ticket with email=" + DEFAULT_MASTER_EMAIL);
    }

    @Test
    void assignValidMaster() throws ItemWasNotFoundException {
        Long ticketId = this.TICKET.getId();
        Ticket ticket = this.ticketService.assignMaster(ticketId, DEFAULT_MASTER_EMAIL);
        assertNotNull(ticket, "Couldn't update ticket.");
        assertEquals(ticket.getMasterEmail(), DEFAULT_MASTER_EMAIL, "Master email must be the same.");
    }

    @Test
    void assignMasterForInvalidTicket() {
        Long invalidTicketId = -1L;
        Exception exception = assertThrows(ItemWasNotFoundException.class, () -> {
            this.ticketService.assignMaster(invalidTicketId, DEFAULT_MASTER_EMAIL);
        });

        String expectedMessage = "Ticket with " + invalidTicketId + " was not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAll() {
        List<Ticket> tickets = this.ticketRepository.findAll();
        assertEquals(TICKETS.size(), tickets.size(), "Repository and tickets have diff sizes.");
        assertNotEquals(tickets.size(), 0, "Tickets are empty.");
    }

    @Test
    void updateTicketPrice() throws ItemWasNotFoundException {
        assertEquals(this.TICKET.getPrice().compareTo(BigDecimal.TEN), 0, "ticket's price is not 0.");
        TicketDto ticketDto = new TicketDto();
        ticketDto.setPrice(BigDecimal.TEN);
        Ticket ticket = this.ticketService.updateTicketPrice(this.TICKET.getId(), ticketDto);
        assertEquals(ticket.getId(), this.TICKET.getId(), "Updated price for wrong ticket.");
        assertEquals(0, ticket.getPrice().compareTo(BigDecimal.TEN), "Balance was not topped up.");
    }

    @Test
    void updateInvalidTicketPrice() {
        assertEquals(this.TICKET.getPrice().compareTo(BigDecimal.TEN), 0, "ticket's price is not 0.");
        BigDecimal invalidValue = BigDecimal.valueOf(-1);
        TicketDto ticketDto = new TicketDto();
        ticketDto.setPrice(invalidValue);
        Exception exception = assertThrows(TransactionSystemException.class, () -> {
            this.ticketService.updateTicketPrice(this.TICKET.getId(), ticketDto);
        });

        String actualMessage = exception.getMessage();
        assertNotNull(actualMessage);
    }

    @Test
    void updateStatus() throws InvalidStatusChangeException, ItemWasNotFoundException, UnvalidStatusUpdateException {
        String invalidStatusName = TicketStatus.IN_PROGRESS.getName();
        assertEquals(this.TICKET.getStatus(), TicketStatus.NEW, "ticket must be NEW");
        UserDetailsImpl userDetails = UserDetailsImpl.build(User.builder()
                .email(DEFAULT_MASTER_EMAIL)
                .roles(Set.of(new Role(ERole.ROLE_MASTER)))
                .build());
        this.TICKET = this.ticketService.updateStatus(this.TICKET.getId(), invalidStatusName, userDetails);
        assertNotNull(this.TICKET, "ticket couln't be updated.");
        assertEquals(this.TICKET.getStatus(), TicketStatus.IN_PROGRESS, "ticket must be in progress phase.");
    }

    @Test
    void updateStatusWithInvalidStatusName() {
        String invalidStatusName = "#invalid#";
        UserDetailsImpl userDetails = UserDetailsImpl.build(User.builder()
                .email("#invalid_email#")
                .roles(Set.of(new Role(ERole.ROLE_MASTER)))
                .build());
        Exception exception = assertThrows(ItemWasNotFoundException.class, () -> {
            this.ticketService.updateStatus(this.TICKET.getId(), invalidStatusName, userDetails);
        });

        String expectedMessage = "Updated status is not valid, please specify the correct one.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateStatusWithLackOfPermission() {
        String statusName = TicketStatus.IN_PROGRESS.getName();
        UserDetailsImpl userDetails = UserDetailsImpl.build(User.builder()
                .email("#invalid_email#")
                .roles(Set.of(new Role(ERole.ROLE_MASTER)))
                .build());
        assertNotEquals(userDetails.getEmail(), this.TICKET.getMasterEmail(), "emails must be not equals.");
        Exception exception = assertThrows(RoleLackOfPermissionException.class, () -> {
            this.ticketService.updateStatus(this.TICKET.getId(), statusName, userDetails);
        });

        String expectedMessage = "It seems like you are trying to update not your ticket...";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateStatusWithInvalidStatusChange() {
        String paidStatusName = TicketStatus.PAID.getName();
        UserDetailsImpl userDetails = UserDetailsImpl.build(User.builder()
                .email(DEFAULT_MASTER_EMAIL)
                .roles(Set.of(new Role(ERole.ROLE_MASTER)))
                .build());
        assertNotEquals(userDetails.getUsername(), this.TICKET.getMasterEmail());
        Exception exception = assertThrows(InvalidStatusChangeException.class, () -> {
            this.ticketService.updateStatus(this.TICKET.getId(), paidStatusName, userDetails);
        });

        String expectedMessage = String.format("Invalid status change from %s to %s for current user.",
                this.TICKET.getStatus().getName(), paidStatusName);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}