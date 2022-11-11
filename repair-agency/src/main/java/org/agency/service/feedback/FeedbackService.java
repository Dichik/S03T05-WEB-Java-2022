package org.agency.service.feedback;

import org.agency.entity.Feedback;
import org.agency.repository.feedback.FeedbackRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeedbackService {
    private static final Logger logger = LogManager.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void submit(Long ticketId, String feedbackText) {
        Feedback feedback = new Feedback();
        feedback.setTicketId(ticketId);
        feedback.setText(feedbackText);
        this.feedbackRepository.create(feedback);
    }

}
