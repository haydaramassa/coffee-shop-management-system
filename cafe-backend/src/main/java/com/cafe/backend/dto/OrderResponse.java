package com.cafe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String username;
    private BigDecimal totalAmount;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}