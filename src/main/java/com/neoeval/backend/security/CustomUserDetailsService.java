package com.neoeval.backend.security;

import com.neoeval.backend.entity.User; // Importa tu entidad User
import com.neoeval.backend.repository.UserRepository; // Importa tu UserRepository
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // Para asegurar que la entidad User se cargue completamente
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Permite la autenticación con email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email : " + email));

        return UserPrincipal.create(user);
    }

    // Este método es usado por JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id : " + id));

        return UserPrincipal.create(user);
    }
}