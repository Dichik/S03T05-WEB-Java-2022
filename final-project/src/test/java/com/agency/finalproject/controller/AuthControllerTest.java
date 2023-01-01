package com.agency.finalproject.controller;

import com.agency.finalproject.entity.feedback.Feedback;
import com.agency.finalproject.repository.user.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void authenticateUser() {

    }

    @Test
    void registerUser() throws Exception {
        String username = "user";
        String password = "password";
        String email = "user@user.com";
        String roles = "[\"USER\"]";

        String body = "{\"username\": \"" + username + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"roles\": " + roles + "," +
                "\"password\": \"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void logoutUser() {
    }

}