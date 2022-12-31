package com.agency.finalproject.controller;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.TicketWrongDataException;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.feedback.FeedbackService;
import com.agency.finalproject.service.ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Secured({"ROLE_USER"})
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
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/feedback", method = RequestMethod.GET, params = {"ticketId"})
    public ResponseEntity<?> showFeedbackAboutTicket(@RequestParam Long ticketId) {
        try {
            List<Feedback> feedbacks = this.feedbackService.findByTicketId(ticketId);
            if (feedbacks.isEmpty()) {
                return new ResponseEntity<>("No feedbacks were found for this ticket.", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (ItemWasNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
