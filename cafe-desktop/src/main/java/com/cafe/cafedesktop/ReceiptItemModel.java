package com.cafe.cafedesktop;

public class ReceiptItemModel {

    private String productName;
    private Integer quantity;
    private String unitPrice;
    private String lineTotal;

    public ReceiptItemModel(String productName, Integer quantity, String unitPrice, String lineTotal) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getLineTotal() {
        return lineTotal;
    }
}