package com.neoeval.backend.service;

import com.neoeval.backend.dto.response.ParentResponse;
import com.neoeval.backend.dto.request.UpdateUserRequest; // Podrías necesitar un DTO específico para actualizar padres, o usar UpdateUserRequest
import com.neoeval.backend.dto.request.RegisterRequest; // Si usas un RegisterRequest para crear

import java.util.List;

public interface ParentService {
    // Métodos CRUD básicos para Parent
    ParentResponse createParent(RegisterRequest registerRequest); // O un DTO de request específico para Parent
    ParentResponse getParentById(Long id);
    List<ParentResponse> getAllParents();
    ParentResponse updateParent(Long id, UpdateUserRequest updateRequest); // O un DTO de request específico para Parent
    void deleteParent(Long id);

    // Método específico para vincular estudiante
    ParentResponse linkStudentToParent(Long parentId, Long studentId);
}