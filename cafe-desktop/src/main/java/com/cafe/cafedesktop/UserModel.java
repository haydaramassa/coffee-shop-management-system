package com.cafe.cafedesktop;

public class UserModel {

    private Long id;
    private String username;
    private String role;
    private String status;
    private String createdAt;
    private Boolean enabled;

    public UserModel(Long id, String username, String role, String status, String createdAt, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}