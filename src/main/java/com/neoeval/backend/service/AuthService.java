package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.LoginRequest;
import com.neoeval.backend.dto.request.RegisterRequest;
import com.neoeval.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticateUser(LoginRequest loginRequest);
    AuthResponse registerUser(RegisterRequest registerRequest);
    AuthResponse refreshToken(String refreshToken);
    void logoutUser(String token);
}