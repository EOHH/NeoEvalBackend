package com.neoeval.backend.repository;

import com.neoeval.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndUserType(Long id, String userType);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByUserType(String userType);

    // ✅ NUEVOS MÉTODOS PARA APROBACIÓN
    List<User> findByApprovalStatus(String approvalStatus);

    List<User> findByApprovalStatusAndUserType(String approvalStatus, String userType);

    long countByApprovalStatus(String approvalStatus);
}
