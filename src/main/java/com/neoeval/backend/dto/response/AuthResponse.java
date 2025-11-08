// Archivo: com/neoeval/backend/dto/response/AuthResponse.java

package com.neoeval.backend.dto.response;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String userType;
    private String name;
    private String email;
    private Long studentId;
    private String approvalStatus;

    // Constructors
    public AuthResponse() {
    }

    public AuthResponse(String token, Long userId, String userType, String name, String email) {
        this.token = token;
        this.userId = userId;
        this.userType = userType;
        this.name = name;
        this.email = email;
    }

    public AuthResponse(String token, Long userId, String userType, String name, String email, Long studentId) {
        this.token = token;
        this.userId = userId;
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}