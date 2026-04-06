package com.cafe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalUsers;
    private long totalCategories;
    private long totalProducts;
    private long totalCustomers;
    private long totalOrders;
    private BigDecimal totalSales;
}