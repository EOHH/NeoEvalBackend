package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);

    // Buscar admins por nivel
    java.util.List<Admin> findByAdminLevel(String adminLevel);

    // Buscar admins por departamento
    java.util.List<Admin> findByDepartment(String department);
}