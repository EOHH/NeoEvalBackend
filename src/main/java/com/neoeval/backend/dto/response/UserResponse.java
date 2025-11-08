package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.User;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String userType;
    private boolean active;
    private String createdAt;
    private String lastLogin; // ✅ Agregado

    // ✅ CAMPOS DE APROBACIÓN
    private String approvalStatus;
    private Long approvedBy;
    private String approvedAt;
    private String rejectionReason;

    // ✅ CAMPO PARA PADRES
    private Long studentId;

    // Constructor que recibe User
    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userType = user.getUserType();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        this.lastLogin = user.getLastLogin() != null ? user.getLastLogin().toString() : null;

        // ✅ MAPEO DE CAMPOS DE APROBACIÓN
        this.approvalStatus = user.getApprovalStatus();
        this.approvedBy = user.getApprovedBy();
        this.approvedAt = user.getApprovedAt() != null ? user.getApprovedAt().toString() : null;
        this.rejectionReason = user.getRejectionReason();
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    // ✅ GETTERS Y SETTERS DE APROBACIÓN
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    // ✅ GETTER Y SETTER DE STUDENTID
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
