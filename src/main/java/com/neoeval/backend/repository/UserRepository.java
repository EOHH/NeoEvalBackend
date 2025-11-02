package com.neoeval.backend.repository;

import com.neoeval.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List; // Agregado para getAllTeachers/Students/Parents si se decide usar

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método para buscar un usuario por ID y tipo de usuario
    Optional<User> findByIdAndUserType(Long id, String userType);

    // Método para verificar si un correo electrónico ya existe
    boolean existsByEmail(String email);

    // *** ¡AÑADIDO! Método para encontrar un usuario por su correo electrónico ***
    Optional<User> findByEmail(String email);

    // Método para encontrar todos los usuarios por tipo (útil para getAllTeachers/Students/Parents si se manejan todos desde User)
    List<User> findByUserType(String userType);

    // Puedes añadir otros métodos personalizados si los necesitas aquí
}