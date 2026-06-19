package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.QuizSubmissionRequest;
import com.neoeval.backend.dto.request.UpdateResultScoreRequest; // 🚀 IMPORTACIÓN CLAVE
import com.neoeval.backend.dto.response.StudentResultResponse;
import com.neoeval.backend.dto.response.StudentExamResultDetailResponse;
import com.neoeval.backend.entity.StudentResult;
import com.neoeval.backend.service.StudentResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // 🚀 IMPORTACIÓN CLAVE para validación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("api/student-results")
public class StudentResultController {

    private final StudentResultService studentResultService;

    public StudentResultController(StudentResultService studentResultService) {
        this.studentResultService = studentResultService;
    }

    // Endpoint: POST /api/student-results/submit
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Long>> submitExam(@RequestBody QuizSubmissionRequest request) {
        StudentResult result = studentResultService.processQuizSubmission(request);
        Map<String, Long> response = Collections.singletonMap("resultId", result.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint: GET /api/student-results/student/{studentId} (Historial)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<StudentResultResponse>> getResultsByStudent(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<StudentResultResponse> results = studentResultService.getResultsByStudent(studentId, pageable);
        return ResponseEntity.ok(results);
    }

    // 🚀 ENDPOINT 1: Resultados por Examen (Para la lista del profesor)
    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Page<StudentExamResultDetailResponse>> getStudentResultsByExam(
            @PathVariable Long examId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<StudentExamResultDetailResponse> results = studentResultService.getStudentResultsByExam(examId, pageable);
        return ResponseEntity.ok(results);
    }

    // =====================================================================
    // 🚀 NUEVO ENDPOINT PARA ACTUALIZAR NOTA MANUALMENTE
    // =====================================================================
    /**
     * Permite al profesor ajustar la puntuación (score) de un resultado manualmente.
     * URL: PUT /api/student-results/{resultId}/score
     */
    @PutMapping("/{resultId}/score")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')") // Solo profesor/admin
    public ResponseEntity<StudentExamResultDetailResponse> updateResultScore(
            @PathVariable Long resultId,
            @Valid @RequestBody UpdateResultScoreRequest request) {

        StudentExamResultDetailResponse updatedResult = studentResultService.updateResultScore(resultId, request);

        return ResponseEntity.ok(updatedResult);
    }
}