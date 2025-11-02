package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.CreateExamRequest;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.ExamSummaryResponse; // 👈 NUEVA IMPORTACIÓN

import java.util.List;

public interface ExamService {
    ExamResponse createExam(CreateExamRequest examRequest);
    ExamResponse getExamById(Long id);
    List<ExamResponse> getExamsByGroup(Long groupId);
    List<ExamResponse> getExamsByTeacher(Long teacherId);

    // ✅ NUEVO: Métodos para obtener exámenes del estudiante
    List<ExamResponse> getAvailableExamsForStudent(Long studentId);
    List<ExamResponse> getStudentExamHistory(Long studentId);

    // =================================================================
    // 🚀 NUEVO MÉTODO PARA EL RESUMEN DEL PROFESOR
    // =================================================================
    /**
     * Obtiene una lista de resúmenes de resultados para todos los exámenes
     * creados por el profesor especificado (agregación de resultados).
     * @param teacherId El ID del profesor.
     * @return Lista de ExamSummaryResponse con datos agregados.
     */
    List<ExamSummaryResponse> getExamSummariesByTeacher(Long teacherId);

    ExamResponse updateExam(Long id, CreateExamRequest examRequest);
    void deleteExam(Long id);
}