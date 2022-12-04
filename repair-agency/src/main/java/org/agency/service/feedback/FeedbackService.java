package org.agency.service.feedback;

import org.agency.delegator.RepositoryDelegator;
import org.agency.delegator.ServiceDelegator;
import org.agency.entity.Feedback;
import org.agency.repository.feedback.FeedbackRepository;
import org.agency.service.BaseService;
import org.agency.service.session.CurrentSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeedbackService implements BaseService {
    private static final Logger logger = LogManager.getLogger(FeedbackService.class);

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(RepositoryDelegator repositoryDelegator) throws ClassNotFoundException {
        this.feedbackRepository = (FeedbackRepository) repositoryDelegator.getByClass(FeedbackRepository.class);
    }

    public void submit(Long ticketId, String feedbackText) {
        Feedback feedback = new Feedback.FeedbackBuilder(feedbackText)
                .setTicketId(ticketId)
                .setUserEmail(CurrentSession.getSession().getEmail())
                .build();
        this.feedbackRepository.create(feedback);
        logger.info(String.format("Feedback to ticket with id=[%d] was successfully added.", ticketId));
    }

}
