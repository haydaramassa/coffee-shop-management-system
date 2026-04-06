package com.cafe.backend.service;

import com.cafe.backend.dto.DashboardResponse;
import com.cafe.backend.repository.CategoryRepository;
import com.cafe.backend.repository.CustomerRepository;
import com.cafe.backend.repository.OrderRepository;
import com.cafe.backend.repository.ProductRepository;
import com.cafe.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public DashboardResponse getDashboardStats() {
        BigDecimal totalSales = orderRepository.findTotalSalesByActiveTrue();
        if (totalSales == null) {
            totalSales = BigDecimal.ZERO;
        }

        return DashboardResponse.builder()
                .totalUsers(userRepository.countByEnabledTrue())
                .totalCategories(categoryRepository.countByActiveTrue())
                .totalProducts(productRepository.countByActiveTrue())
                .totalCustomers(customerRepository.countByActiveTrue())
                .totalOrders(orderRepository.countByActiveTrue())
                .totalSales(totalSales)
                .build();
    }
    public java.util.List<com.cafe.backend.dto.ChartDataResponse> getDailySalesChart() {
        return orderRepository.findDailySalesSummary()
                .stream()
                .map(row -> com.cafe.backend.dto.ChartDataResponse.builder()
                        .label(String.valueOf(row[0]))
                        .value(row[1] == null ? 0.0 : ((java.math.BigDecimal) row[1]).doubleValue())
                        .build())
                .toList();
    }

    public java.util.List<com.cafe.backend.dto.ChartDataResponse> getDailyOrdersChart() {
        return orderRepository.findDailyOrdersSummary()
                .stream()
                .map(row -> com.cafe.backend.dto.ChartDataResponse.builder()
                        .label(String.valueOf(row[0]))
                        .value(row[1] == null ? 0.0 : ((Long) row[1]).doubleValue())
                        .build())
                .toList();
    }
}