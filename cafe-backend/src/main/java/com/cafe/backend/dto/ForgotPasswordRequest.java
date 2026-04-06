package com.cafe.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {

    private String username;
    private String securityQuestion;
    private String securityAnswer;
    private String newPassword;
}