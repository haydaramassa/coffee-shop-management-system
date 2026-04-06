package com.cafe.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateUserRequest {

    private String username;
    private String password;
    private String role;
    private String securityQuestion;
    private String securityAnswer;
}