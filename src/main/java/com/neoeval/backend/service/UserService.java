package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.*; // Asegúrate de que UserResponse, StudentResponse, ParentResponse estén aquí

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    // Eliminado: TeacherResponse getTeacherById(Long id); // Mueve a TeacherService
    StudentResponse getStudentById(Long id);
    ParentResponse getParentById(Long id);
    List<StudentResponse> getAllStudents();
    // Eliminado: List<TeacherResponse> getAllTeachers(); // Mueve a TeacherService
    UserResponse updateUser(Long id, UpdateUserRequest updateRequest);
    void deleteUser(Long id);

    UserResponse toggleUserStatus(Long id, boolean active);
}