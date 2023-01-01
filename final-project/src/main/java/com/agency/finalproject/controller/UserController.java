package com.agency.finalproject.controller;

import com.agency.finalproject.entity.login.response.MessageResponse;
import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.exception.ItemWasNotFoundException;
import com.agency.finalproject.exception.NotEnoughMoneyException;
import com.agency.finalproject.security.service.UserDetailsImpl;
import com.agency.finalproject.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<?> getBalance(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            User user = this.userService.findById(userDetails.id())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=[%d] was not found.", userDetails.id())));
            return new ResponseEntity<>(new MessageResponse("Your current balance is: " + user.getBalance()), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String message = String.format("Couldn't get current balance for user_id=[%d]", userDetails.id());
            logger.error(message + ", see: " + e);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST, params = {"ticketId"})
    public ResponseEntity<?> payForTicket(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getEmail();
        try {
            User user = this.userService.payForTicket(id, email);

            Map<String, Object> body = new LinkedHashMap<>(){{
               put("data", user);
               put("message", "Ticket was successfully paid!");
            }};
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            String message = String.format("User with email=[%s] couldn't pay for ticket with id=[%d]", email, id);
            logger.warn(message);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } catch (NotEnoughMoneyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ItemWasNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(value = "/topup", method = RequestMethod.POST, params = {"username", "amount"})
    public ResponseEntity<?> topUpAccount(@RequestParam String username, @RequestParam BigDecimal amount) {
        try {
            User user = this.userService.topUpBalance(username, amount);
            String message = String.format("Balance of user with username=[%s] was topped up with amount=[%f]", username, amount);

            Map<String, Object> body = new LinkedHashMap<>() {{
                put("data", user);
                put("message", message);
            }};
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Couldn't top up account, see: " + e);
            return new ResponseEntity<>("Couldn't top up accont, please try again.", HttpStatus.NOT_FOUND);
        }
    }

}
