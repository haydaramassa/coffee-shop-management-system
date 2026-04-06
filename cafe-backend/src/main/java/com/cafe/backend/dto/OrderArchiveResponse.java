package com.cafe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderArchiveResponse {

    private Long orderId;
    private String customerName;
    private String cashierName;
    private Double totalAmount;
    private String createdAt;
}