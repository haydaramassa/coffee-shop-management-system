package com.cafe.cafedesktop;

public class ReceiptArchiveModel {

    private Long receiptId;
    private String customerId;
    private String date;
    private Double total;
    private String cashier;

    public ReceiptArchiveModel(Long receiptId, String customerId, String date, Double total, String cashier) {
        this.receiptId = receiptId;
        this.customerId = customerId;
        this.date = date;
        this.total = total;
        this.cashier = cashier;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public Double getTotal() {
        return total;
    }

    public String getCashier() {
        return cashier;
    }
}
