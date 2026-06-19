package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.LoginRequest;
import com.neoeval.backend.dto.request.RegisterRequest;
import com.neoeval.backend.dto.response.ApiResponse;
import com.neoeval.backend.dto.response.AuthResponse;
import com.neoeval.backend.exception.AuthenticationException;
import com.neoeval.backend.security.SecurityConstants;
import com.neoeval.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

            // 🔥 CREAR LA COOKIE HttpOnly PARA LA WEB
            ResponseCookie jwtCookie = ResponseCookie.from(SecurityConstants.COOKIE_NAME, authResponse.getToken())
                    .httpOnly(true)
                    .secure(false) // PONER EN 'true' EN PRODUCCIÓN HTTPS
                    .path("/")
                    .maxAge(SecurityConstants.EXPIRATION_TIME / 1000) // Convertir milisegundos a segundos
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString()) // Añadimos la cookie
                    .body(new ApiResponse(true, "Inicio de sesión exitoso", authResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = authService.registerUser(registerRequest);

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

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String email, @RequestParam String otp) {
        try {
            authService.verifyEmail(email, otp);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Correo electrónico verificado con éxito. Ahora debes esperar la aprobación del administrador.", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = SecurityConstants.COOKIE_NAME, required = false) String cookieToken) {
        try {
            // 🔥 LÓGICA DUAL: Leer de Cookie o de Header
            String jwt = null;
            if (cookieToken != null && !cookieToken.isEmpty()) {
                jwt = cookieToken; // Viene de la Web
            } else if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                jwt = authHeader.replace(SecurityConstants.TOKEN_PREFIX, ""); // Viene de la App Móvil
            }

            if (jwt == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Token de actualización mal formado o ausente", null));
            }

            AuthResponse authResponse = authService.refreshToken(jwt);

            // 🔥 ACTUALIZAR LA COOKIE CON EL NUEVO TOKEN
            ResponseCookie jwtCookie = ResponseCookie.from(SecurityConstants.COOKIE_NAME, authResponse.getToken())
                    .httpOnly(true)
                    .secure(false) // PONER EN 'true' EN PRODUCCIÓN HTTPS
                    .path("/")
                    .maxAge(SecurityConstants.EXPIRATION_TIME / 1000)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ApiResponse(true, "Token refrescado exitosamente", authResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @CookieValue(value = SecurityConstants.COOKIE_NAME, required = false) String cookieToken) {
        try {
            // 🔥 LÓGICA DUAL: Leer de Cookie o de Header
            String jwt = null;
            if (cookieToken != null && !cookieToken.isEmpty()) {
                jwt = cookieToken; // Viene de la Web
            } else if (authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                jwt = authHeader.replace(SecurityConstants.TOKEN_PREFIX, ""); // Viene de la App Móvil
            }

            if (jwt != null) {
                authService.logoutUser(jwt);
            }

            // 🔥 CREAR COOKIE VACÍA PARA BORRARLA DEL NAVEGADOR WEB
            ResponseCookie cleanCookie = ResponseCookie.from(SecurityConstants.COOKIE_NAME, "")
                    .httpOnly(true)
                    .secure(false) // PONER EN 'true' EN PRODUCCIÓN HTTPS
                    .path("/")
                    .maxAge(0) // 0 le dice al navegador que la elimine de inmediato
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                    .body(new ApiResponse(true, "Sesión cerrada exitosamente", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}