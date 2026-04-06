package com.cafe.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyResetRequest {

    private String username;
    private String securityQuestion;
    private String securityAnswer;
}