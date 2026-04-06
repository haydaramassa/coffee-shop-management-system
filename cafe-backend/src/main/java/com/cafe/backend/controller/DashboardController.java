package com.cafe.backend.controller;

import com.cafe.backend.dto.DashboardResponse;
import com.cafe.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
    @GetMapping("/sales-chart")
    public java.util.List<com.cafe.backend.dto.ChartDataResponse> getDailySalesChart() {
        return dashboardService.getDailySalesChart();
    }

    @GetMapping("/orders-chart")
    public java.util.List<com.cafe.backend.dto.ChartDataResponse> getDailyOrdersChart() {
        return dashboardService.getDailyOrdersChart();
    }
}