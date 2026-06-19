package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.CreateExamRequest;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.ExamSummaryResponse; // 👈 NUEVA IMPORTACIÓN

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    ExamResponse createExam(CreateExamRequest examRequest);
    ExamResponse getExamById(Long id);
    Page<ExamResponse> getExamsByGroup(Long groupId, Pageable pageable);
    Page<ExamResponse> getExamsByTeacher(Long teacherId, Pageable pageable);

    // ✅ NUEVO: Métodos para obtener exámenes del estudiante
    Page<ExamResponse> getAvailableExamsForStudent(Long studentId, Pageable pageable);
    Page<ExamResponse> getStudentExamHistory(Long studentId, Pageable pageable);

    // =================================================================
    // 🚀 NUEVO MÉTODO PARA EL RESUMEN DEL PROFESOR
    // =================================================================
    /**
     * Obtiene una lista de resúmenes de resultados para todos los exámenes
     * creados por el profesor especificado (agregación de resultados).
     * @param teacherId El ID del profesor.
     * @return Lista de ExamSummaryResponse con datos agregados.
     */
    Page<ExamSummaryResponse> getExamSummariesByTeacher(Long teacherId, Pageable pageable);

    ExamResponse updateExam(Long id, CreateExamRequest examRequest);
    void deleteExam(Long id);
}