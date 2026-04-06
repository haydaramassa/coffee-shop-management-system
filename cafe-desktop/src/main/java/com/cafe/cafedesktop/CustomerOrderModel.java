package com.cafe.cafedesktop;

public class CustomerOrderModel {

    private Long orderId;
    private Double totalAmount;
    private String cashierName;
    private String createdAt;

    public CustomerOrderModel(Long orderId, Double totalAmount, String cashierName, String createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.cashierName = cashierName;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getCashierName() {
        return cashierName;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}