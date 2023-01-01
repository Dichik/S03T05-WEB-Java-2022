package com.agency.finalproject.controller;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.TicketWrongDataException;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.feedback.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST, params = {"ticketId", "text"})
    public ResponseEntity<?> leaveFeedback(@RequestParam Long ticketId, @RequestParam String text,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Feedback feedback = this.feedbackService.submit(userDetails.getUsername(), ticketId, text);

            Map<String, Object> body = new LinkedHashMap<>() {{
                put("data", feedback);
                put("message", "Feedback was successfully submitted!");
            }};
            return new ResponseEntity<>(body, HttpStatus.CREATED);
        } catch (TicketWrongDataException e) {
            log.error("Can't leave feedback, see: " + e);
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.GET, params = {"ticketId"})
    public ResponseEntity<?> showFeedbackAboutTicket(@RequestParam Long ticketId) {
        try {
            List<Feedback> feedbacks = this.feedbackService.findByTicketId(ticketId);
            if (feedbacks.isEmpty()) {
                return new ResponseEntity<>("No feedbacks were found for this ticket.", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (ItemWasNotFoundException e) {
            log.warn("Ticket was not found, see: " + e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
