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
public class ReceiptResponse {

    private Long orderId;
    private String customerName;
    private String cashierName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<ReceiptItemResponse> items;
}