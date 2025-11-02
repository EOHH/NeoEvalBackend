package com.neoeval.backend.dto.response;

import java.time.Instant;
import java.util.Date;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String userType; // TEACHER, STUDENT, PARENT
    private Instant createdAt;
    private Instant lastLogin;
    private boolean active;
    private Long studentId;

    // Constructor vacío (necesario para la deserialización de frameworks como Spring)
    public UserResponse() {}

    // Constructor completo para facilitar el mapeo desde la entidad User
    public UserResponse(Long id, String name, String email, String userType, Instant createdAt, Instant lastLogin, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.active = active;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}