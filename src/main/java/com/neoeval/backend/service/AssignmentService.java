package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.AssignExamRequest; // ¡Usar este DTO!
import com.neoeval.backend.dto.request.SubmitAnswerRequest;
import com.neoeval.backend.dto.response.AssignmentResponse;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse getAssignmentById(Long id);

    List<AssignmentResponse> getAssignmentsByStudentId(Long studentId);
    List<AssignmentResponse> getAssignmentsByExamId(Long examId);

    // Método para que el estudiante envíe sus respuestas a una asignación
    AssignmentResponse submitAssignment(Long assignmentId, List<SubmitAnswerRequest> answers);

    // Método para asignar un examen a múltiples estudiantes (usando tu AssignExamRequest)
    List<AssignmentResponse> assignExamToStudents(AssignExamRequest request); // Cambiado para usar el DTO y devolver una lista

    // Método para calificar una asignación
    AssignmentResponse gradeAssignment(Long assignmentId, Double score);
}