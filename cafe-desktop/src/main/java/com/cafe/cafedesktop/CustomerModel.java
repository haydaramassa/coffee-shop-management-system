package com.cafe.cafedesktop;

public class CustomerModel {

    private Long id;
    private String name;
    private String phone;
    private String notes;
    private String createdAt;

    public CustomerModel(Long id, String name, String phone, String notes, String createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return name == null || name.isBlank() ? "Walk-in Customer" : name;
    }
}