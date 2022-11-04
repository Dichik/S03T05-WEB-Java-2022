package org.example.controller;

import org.example.entity.Ticket;
import org.example.service.UserService;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // TODO research how to perform one method before execution another (like filter?)

    public void submitTicket(Ticket ticket, Long userId) {
        // TODO implement submitTicket() method
    }



}
