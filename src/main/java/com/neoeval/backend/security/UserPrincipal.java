package com.neoeval.backend.security;

import com.neoeval.backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String userType; // STUDENT, TEACHER, ADMIN, etc.
    private boolean active;  // 👈 útil si tu entidad User tiene un campo "active"
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor completo
    public UserPrincipal(Long id, String name, String email, String password, String userType, boolean active,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.active = active;
        this.authorities = authorities;
    }

    // 🔹 Método estático para crear un UserPrincipal desde tu entidad User
    public static UserPrincipal create(User user) {
        // Asignar el rol en formato Spring Security ("ROLE_ADMIN", "ROLE_TEACHER", etc.)
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getUserType().toUpperCase())
        );

        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.isActive(), // 👈 si tu entidad tiene este campo
                authorities
        );
    }

    // 🔹 Getters personalizados
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    // Métodos de la interfaz UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Spring usa este método como "username" — aquí devolvemos el email
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 🔹 Retorna si el usuario está activo
    @Override
    public boolean isEnabled() {
        return active;
    }

    // Métodos equals/hashCode (importantes para comparar instancias en autenticación)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
