package com.cafe.cafedesktop;

public class OrderItemRequestModel {

    private Long productId;
    private Integer quantity;

    public OrderItemRequestModel(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}