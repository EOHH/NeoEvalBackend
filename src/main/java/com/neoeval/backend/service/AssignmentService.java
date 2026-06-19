package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.AssignExamRequest; // ¡Usar este DTO!
import com.neoeval.backend.dto.request.SubmitAnswerRequest;
import com.neoeval.backend.dto.response.AssignmentResponse;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    AssignmentResponse getAssignmentById(Long id);

    Page<AssignmentResponse> getAssignmentsByStudentId(Long studentId, Pageable pageable);
    Page<AssignmentResponse> getAssignmentsByExamId(Long examId, Pageable pageable);

    // Método para que el estudiante envíe sus respuestas a una asignación
    AssignmentResponse submitAssignment(Long assignmentId, List<SubmitAnswerRequest> answers);

    // Método para asignar un examen a múltiples estudiantes (usando tu AssignExamRequest)
    List<AssignmentResponse> assignExamToStudents(AssignExamRequest request); // Cambiado para usar el DTO y devolver una lista

    // Método para calificar una asignación
    AssignmentResponse gradeAssignment(Long assignmentId, Double score);
}