package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(Long id);

    StudentResponse getStudentById(Long id);

    ParentResponse getParentById(Long id);

    Page<StudentResponse> getAllStudents(Pageable pageable);

    UserResponse updateUser(Long id, UpdateUserRequest updateRequest);

    void deleteUser(Long id);

    UserResponse toggleUserStatus(Long id, boolean active);

    // ✅ NUEVOS MÉTODOS PARA APROBACIÓN
    Page<UserResponse> getPendingUsers(Pageable pageable);

    UserResponse approveUser(Long userId, Long adminId);

    UserResponse rejectUser(Long userId, Long adminId, String reason);
}
