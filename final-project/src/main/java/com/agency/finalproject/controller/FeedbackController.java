package com.agency.finalproject.controller;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.ticket.Ticket;
import com.agency.finalproject.entity.ticket.TicketStatus;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final TicketService ticketService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, TicketService ticketService) {
        this.feedbackService = feedbackService;
        this.ticketService = ticketService;
    }

    @Secured({"ROLE_USER"})
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST, params = {"ticketId", "text"})
    public ResponseEntity<?> leaveFeedback(@RequestParam Long ticketId, @RequestParam String text,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Ticket> ticket = this.ticketService.getById(ticketId);
        if (ticket.isEmpty() || ticket.get().getStatus() != TicketStatus.DONE) { // FIXME
            return new ResponseEntity<>(new MessageResponse("Couldn't leave your feedback, try again."), HttpStatus.BAD_REQUEST);
        }

        Feedback feedback = this.feedbackService.submit(userDetails.getUsername(), ticketId, text);
        Map<String, Object> body = new LinkedHashMap<>() {{
            put("data", feedback);
            put("message", "Feedback was successfully submitted!");
        }};
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

}
