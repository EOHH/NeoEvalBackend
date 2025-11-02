package com.neoeval.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String jwtSecret = SecurityConstants.SECRET;
    private final long jwtExpirationInMs = SecurityConstants.EXPIRATION_TIME;

    // 🔹 Generar token desde Authentication (usando email como subject)
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // normalmente el email
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Generar token desde UserPrincipal (usando ID como subject y rol como claim)
    public String generateToken(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Obtener el rol del usuario (ejemplo: ROLE_TEACHER)
        String role = userPrincipal.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse("ROLE_USER");

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("role", role) // 👈 Guardamos el rol dentro del token
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Obtener ID de usuario desde el token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    // 🔹 Obtener el rol del token (opcional)
    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    // 🔹 Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            logger.error("Firma JWT inválida", ex);
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT mal formado", ex);
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expirado", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT no soportado", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("La cadena JWT está vacía", ex);
        }
        return false;
    }

    // 🔹 Clave segura para firmar el token
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
