package com.agency.finalproject.security;

import com.agency.finalproject.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSignupForValidUser() throws Exception {
        String username = "user";
        String password = "password";
        String email = "user@user.com";
        Set<String> roles = Set.of("USER");

        String body = "{\"username\": \"" + username + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"roles\": " + roles + "," +
                "\"password\": \"" + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
        String username = "existentuser";
        String password = "password";

        String body = "{\"username\":\"" + username + ",\"password\":\""
                + password + "\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        response = response.replace("{\"access_token\": \"", "");
        String token = response.replace("\"}", "");

        mvc.perform(MockMvcRequestBuilders.get("/test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void nonexistentUserCannotGetToken() throws Exception {
        String username = "nonexistentuser";
        String password = "password";

        String body = "{\"username\":\"" + username + "\", \"password\":\""
                + password + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/v2/token")
                        .content(body))
                .andExpect(status().isForbidden()).andReturn();
    }

}
