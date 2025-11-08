package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.LoginRequest;
import com.neoeval.backend.dto.request.RegisterRequest;
import com.neoeval.backend.dto.response.ApiResponse;
import com.neoeval.backend.dto.response.AuthResponse;
import com.neoeval.backend.exception.AuthenticationException;
import com.neoeval.backend.security.SecurityConstants;
import com.neoeval.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Inicio de sesión exitoso", authResponse)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = authService.registerUser(registerRequest);

            // ✅ Verificar si el usuario está pendiente de aprobación
            if ("PENDING".equals(authResponse.getApprovalStatus())) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse(
                                true,
                                "Registro exitoso. Tu cuenta está pendiente de aprobación por un administrador. Te notificaremos cuando sea revisada.",
                                authResponse
                        ));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Usuario registrado exitosamente", authResponse));

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Token de actualización mal formado o ausente", null));
            }

            String token = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
            AuthResponse authResponse = authService.refreshToken(token);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Token refrescado exitosamente", authResponse)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Token mal formado o ausente", null));
            }

            String jwt = authHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
            authService.logoutUser(jwt);
            return ResponseEntity.ok(
                    new ApiResponse(true, "Sesión cerrada exitosamente", null)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}
