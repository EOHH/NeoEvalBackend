package com.neoeval.backend.repository;

import com.neoeval.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndUserType(Long id, String userType);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findByUserType(String userType, Pageable pageable);

    // ✅ NUEVOS MÉTODOS PARA APROBACIÓN
    Page<User> findByApprovalStatus(String approvalStatus, Pageable pageable);

    Page<User> findByApprovalStatusAndUserType(String approvalStatus, String userType, Pageable pageable);

    long countByApprovalStatus(String approvalStatus);
}
