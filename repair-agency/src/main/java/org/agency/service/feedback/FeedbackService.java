package org.agency.service.feedback;

import org.agency.entity.Feedback;
import org.agency.repository.feedback.FeedbackRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService implements BaseService {
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
                .userEmail(CurrentSession.getSession().getEmail())
                .build();
        this.feedbackRepository.create(feedback);
        logger.info(String.format("Feedback to ticket with id=[%d] was successfully added.", ticketId));
    }

}
