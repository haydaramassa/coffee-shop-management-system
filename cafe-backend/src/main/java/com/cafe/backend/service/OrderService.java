package com.cafe.backend.service;

import com.cafe.backend.dto.*;
import com.cafe.backend.entity.*;
import com.cafe.backend.exception.BusinessException;
import com.cafe.backend.exception.ResourceNotFoundException;
import com.cafe.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        Order order = Order.builder()
                .customer(customer)
                .user(user)
                .totalAmount(BigDecimal.ZERO)
                .active(true)
                .build();

        Order savedOrder = orderRepository.save(order);

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new BusinessException("Product is inactive: " + product.getName());
            }

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            if (product.getQuantity() <= 0) {
                product.setQuantity(0);
                product.setActive(false);
            }
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();

            orderItemRepository.save(orderItem);

            itemResponses.add(OrderItemResponse.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build());
        }

        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .customerId(customer != null ? customer.getId() : null)
                .customerName(customer != null ? customer.getName() : null)
                .userId(user.getId())
                .username(user.getUsername())
                .totalAmount(totalAmount)
                .active(savedOrder.getActive())
                .createdAt(savedOrder.getCreatedAt())
                .items(itemResponses)
                .build();
    }
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = orderItemRepository.findByOrderId(order.getId())
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .totalAmount(order.getTotalAmount())
                .active(order.getActive())
                .createdAt(order.getCreatedAt())
                .items(itemResponses)
                .build();
    }
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findByActiveTrue()
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return mapToOrderResponse(order);
    }
    public ReceiptResponse getReceiptByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<ReceiptItemResponse> receiptItems = orderItemRepository.findByOrderId(order.getId())
                .stream()
                .map(item -> ReceiptItemResponse.builder()
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .build())
                .toList();

        return ReceiptResponse.builder()
                .orderId(order.getId())
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : "Walk-in Customer")
                .cashierName(order.getUser().getUsername())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(receiptItems)
                .build();
    }
    public List<OrderArchiveResponse> getOrdersArchive() {
        return orderRepository.findByActiveTrue()
                .stream()
                .map(order -> OrderArchiveResponse.builder()
                        .orderId(order.getId())
                        .customerName(order.getCustomer() != null ? order.getCustomer().getName() : "Walk-in Customer")
                        .cashierName(order.getUser().getUsername())
                        .totalAmount(order.getTotalAmount().doubleValue())
                        .createdAt(order.getCreatedAt().toString())
                        .build())
                .toList();
    }
    public List<ReceiptResponse> getFilteredOrdersArchive(String keyword, LocalDateTime fromDate, LocalDateTime toDate) {
        return orderRepository.searchArchive(keyword, fromDate, toDate)
                .stream()
                .map(order -> {
                    List<ReceiptItemResponse> receiptItems = orderItemRepository.findByOrderId(order.getId())
                            .stream()
                            .map(item -> ReceiptItemResponse.builder()
                                    .productName(item.getProduct().getName())
                                    .quantity(item.getQuantity())
                                    .unitPrice(item.getUnitPrice())
                                    .lineTotal(item.getLineTotal())
                                    .build())
                            .toList();

                    return ReceiptResponse.builder()
                            .orderId(order.getId())
                            .customerName(order.getCustomer() != null ? order.getCustomer().getName() : "Walk-in Customer")
                            .cashierName(order.getUser().getUsername())
                            .totalAmount(order.getTotalAmount())
                            .createdAt(order.getCreatedAt())
                            .items(receiptItems)
                            .build();
                })
                .toList();
    }
}