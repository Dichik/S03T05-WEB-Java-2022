package com.agency.finalproject.service.feedback;

import com.agency.finalproject.entity.Feedback;
import com.agency.finalproject.repository.feedback.FeedbackRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private static final Logger logger = LogManager.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void submit(Long ticketId, String feedbackText) {
        Feedback feedback = Feedback.builder()
                .text(feedbackText)
                .ticketId(ticketId)
                .userEmail(null)
                .build();
        this.feedbackRepository.save(feedback);
        logger.info(String.format("Feedback to ticket with id=[%d] was successfully added.", ticketId));
    }

}
