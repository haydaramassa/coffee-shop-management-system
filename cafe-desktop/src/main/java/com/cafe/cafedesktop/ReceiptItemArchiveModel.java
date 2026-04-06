package com.cafe.cafedesktop;

public class ReceiptItemArchiveModel {

    private String customerId;
    private String type;
    private Integer qty;
    private Double price;

    public ReceiptItemArchiveModel(String customerId, String type, Integer qty, Double price) {
        this.customerId = customerId;
        this.type = type;
        this.qty = qty;
        this.price = price;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getType() {
        return type;
    }

    public Integer getQty() {
        return qty;
    }

    public Double getPrice() {
        return price;
    }
}