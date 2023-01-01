package com.agency.finalproject.entity.login.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private final List<String> roles;
    private Long id;
    private String username;
    private String email;

}