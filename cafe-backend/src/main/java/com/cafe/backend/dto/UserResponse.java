package com.cafe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String role;
    private Boolean enabled;
    private LocalDateTime createdAt;
}


