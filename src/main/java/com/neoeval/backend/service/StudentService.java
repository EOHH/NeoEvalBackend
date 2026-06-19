package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.UserRequest; // 👈 ¡CORREGIDO! Usar UserRequest
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.CertificateResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    // --- CRUD BÁSICO ---
    StudentResponse createStudent(UserRequest request); // 👈 Usa UserRequest
    StudentResponse updateStudent(Long id, UserRequest request); // 👈 Usa UserRequest
    void deleteStudent(Long id); // Eliminar

    // --- LECTURA ---
    StudentResponse getStudentById(Long id);
    Page<StudentResponse> getAllStudents(Pageable pageable);
    Page<StudentResponse> getStudentsByGroup(Long groupId, Pageable pageable);
    Page<StudentResponse> searchStudentsByName(String name, Pageable pageable);

    // --- LÓGICA CLAVE DE GAMIFICACIÓN ---
    /**
     * Añade puntos al estudiante y desencadena la asignación de logros.
     * @param studentId ID del estudiante.
     * @param pointsToAdd Puntos a sumar.
     * @return El StudentResponse actualizado.
     */
    StudentResponse addPointsToStudent(Long studentId, double pointsToAdd);
}