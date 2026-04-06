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
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private String notes;
    private Boolean active;
    private LocalDateTime createdAt;
}