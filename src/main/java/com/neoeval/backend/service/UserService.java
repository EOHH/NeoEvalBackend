package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.*;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    StudentResponse getStudentById(Long id);

    ParentResponse getParentById(Long id);

    List<StudentResponse> getAllStudents();

    UserResponse updateUser(Long id, UpdateUserRequest updateRequest);

    void deleteUser(Long id);

    UserResponse toggleUserStatus(Long id, boolean active);

    // ✅ NUEVOS MÉTODOS PARA APROBACIÓN
    List<UserResponse> getPendingUsers();

    UserResponse approveUser(Long userId, Long adminId);

    UserResponse rejectUser(Long userId, Long adminId, String reason);
}
