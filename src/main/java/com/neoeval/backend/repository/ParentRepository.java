package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Necesario para findById
import java.util.List;     // Necesario para findAll

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    // Métodos específicos para Parent
    boolean existsByEmail(String email);
}