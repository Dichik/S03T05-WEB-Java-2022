package com.agency.finalproject.service.feedback;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.repository.feedback.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback submit(String username, Long ticketId, String feedbackText) {
        Feedback feedback = Feedback.builder()
                .text(feedbackText)
                .ticketId(ticketId)
                .username(username)
                .build();
        return this.feedbackRepository.save(feedback);
    }

    public List<Feedback> findByTicketId(Long ticketId) {
        return this.feedbackRepository.findAllByTicketId(ticketId);
    }

}
