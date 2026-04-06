package com.cafe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomerOrderSummaryResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private String cashierName;
    private LocalDateTime createdAt;
}