package com.agency.finalproject.service.feedback;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.TicketWrongDataException;
import com.agency.finalproject.repository.feedback.FeedbackRepository;
import com.agency.finalproject.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, TicketRepository ticketRepository) {
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
    }

    public Feedback submit(String username, Long ticketId, String feedbackText) throws TicketWrongDataException {
        Optional<Ticket> ticket = this.ticketRepository.findById(ticketId);
        if (ticket.isEmpty() || !isTicketCompleted(ticket.get())) {
            throw new TicketWrongDataException("Couldn't leave your feedback cause of ticket data, try again.");
        }

        Feedback feedback = Feedback.builder()
                .text(feedbackText)
                .ticketId(ticketId)
                .username(username)
                .build();
        return this.feedbackRepository.save(feedback);
    }

    private boolean isTicketCompleted(Ticket ticket) {
        TicketStatus status = ticket.getStatus();
        return status == TicketStatus.DONE || status == TicketStatus.PAID
                || status == TicketStatus.WAITING_FOR_PAYMENT;
    }

    public List<Feedback> findByTicketId(Long ticketId) throws ItemWasNotFoundException {
        if (!this.ticketRepository.existsById(ticketId)) {
            throw new ItemWasNotFoundException("Ticket with this id was not found.");
        }
        return this.feedbackRepository.findAllByTicketId(ticketId);
    }

}
