package com.agency.finalproject.service.feedback;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.TicketWrongDataException;
import com.agency.finalproject.repository.feedback.FeedbackRepository;
import com.agency.finalproject.repository.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FeedbackServiceTest {

    private final String DEFAULT_TEXT = "It was great!";
    private final String DEFAULT_USER_EMAIL = "user@user.com";

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private FeedbackService feedbackService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        Feedback feedback = Feedback.builder()
                .text(DEFAULT_TEXT)
                .ticketId(1L)
                .username("user")
                .build();
        Feedback feedback1 = this.feedbackRepository.save(feedback);
        assertNotNull(feedback1, "Couldn't save feedback to database.");

        this.ticket = Ticket.builder()
                .title("Ticket #1")
                .status(TicketStatus.DONE)
                .description("keyboard was broken...")
                .build();
        Ticket ticket1 = this.ticketRepository.save(ticket);
        assertNotNull(ticket1, "Couldn't save ticket to database.");
    }

    @Test
    void submitValid() throws TicketWrongDataException {
        String username = DEFAULT_USER_EMAIL;
        Long ticketId = this.ticket.getId();
        String feedbackText = DEFAULT_TEXT;

        Feedback feedback = this.feedbackService.submit(username, ticketId, feedbackText);
        assertNotNull(feedback, "Couldn't submit feedback");
        assertEquals(username, feedback.getUsername(), "Feedback was not saved properly.");
        assertEquals(ticketId, feedback.getTicketId(), "Feedback was not saved properly.");
        assertEquals(feedbackText, feedback.getText(), "Feedback was not saved properly.");
    }

    @Test
    void submitInvalidWithTicketId() {
        String username = DEFAULT_USER_EMAIL;
        Long invalidTicketId = -1L;
        String feedbackText = DEFAULT_TEXT;

        Exception exception = assertThrows(TicketWrongDataException.class, () -> {
            this.feedbackService.submit(username, invalidTicketId, feedbackText);
        });

        String expectedMessage = "Couldn't leave your feedback cause of ticket data, try again.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void submitInvalidWithStatus() {
        String username = DEFAULT_USER_EMAIL;
        Long invalidTicketId = this.ticket.getId();
        String feedbackText = DEFAULT_TEXT;

        this.ticket.setStatus(TicketStatus.NEW);
        ticket = this.ticketRepository.save(this.ticket);
        assertNotNull(ticket, "Couldn't save ticket.");
        assertEquals(ticket.getStatus(), TicketStatus.NEW, "Status is not expected, fix test.");

        Exception exception = assertThrows(TicketWrongDataException.class, () -> {
            this.feedbackService.submit(username, invalidTicketId, feedbackText);
        });

        String expectedMessage = "Couldn't leave your feedback cause of ticket data, try again.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findByTicketIdInvalid() {
        Long invalidTicketId = -1L;
        Exception exception = assertThrows(ItemWasNotFoundException.class, () -> {
            this.feedbackService.findByTicketId(invalidTicketId);
        });

        String expectedMessage = "Ticket with this id was not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findByTicketIdValid() throws ItemWasNotFoundException {
        Long ticketId = this.ticket.getId();
        List<Feedback> feedbacks = this.feedbackService.findByTicketId(ticketId);

        assertTrue(feedbacks.isEmpty(), "Couldn't find already existed ticket.");
    }

}