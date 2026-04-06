package com.cafe.cafedesktop;

public class ProductModel {

    private Long id;
    private String name;
    private String type;
    private Integer stock;
    private Double price;
    private String status;
    private String date;
    private String imageUrl;

    public ProductModel(Long id, String name, String type, Integer stock, Double price, String status, String date, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}