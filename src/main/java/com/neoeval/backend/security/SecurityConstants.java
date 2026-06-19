package com.neoeval.backend.security;

public class SecurityConstants {
    public static final String SECRET = "YourSuperSecretKeyForJWTGenerationAndValidationThatIsLongEnoughAndSecure"; // ¡CAMBIA ESTO EN PRODUCCIÓN!
    public static final long EXPIRATION_TIME = 864_000_000; // 10 días en milisegundos
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/auth/register"; // URL de registro
    public static final String LOGIN_URL = "/api/auth/login"; // URL de login
    public static final String VERIFY_EMAIL_URL = "/api/auth/verify-email"; // URL de verificación de correo

    public static final String COOKIE_NAME = "jwt_auth";
}