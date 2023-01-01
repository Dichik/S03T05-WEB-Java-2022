package com.agency.finalproject.controller;

import com.agency.finalproject.entity.user.User;
import com.agency.finalproject.service.master.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/masters")
public class MasterController {

    private final MasterService masterService;

    @Autowired
    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getTickets() {
        List<User> masters = this.masterService.findAllMasters();
        if (masters.isEmpty()) {
            return new ResponseEntity<>("No masters were found.", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(masters, HttpStatus.OK);
    }

}
