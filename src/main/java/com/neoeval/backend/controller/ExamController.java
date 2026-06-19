package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.CreateExamRequest;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.ExamSummaryResponse; // 👈 ¡NUEVA IMPORTACIÓN!
import com.neoeval.backend.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> createExam(@Valid @RequestBody CreateExamRequest examRequest) {
        ExamResponse response = examService.createExam(examRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable Long id) {
        ExamResponse response = examService.getExamById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<Page<ExamResponse>> getExamsByGroup(
            @PathVariable Long groupId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamResponse> responses = examService.getExamsByGroup(groupId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Page<ExamResponse>> getExamsByTeacher(
            @PathVariable Long teacherId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamResponse> responses = examService.getExamsByTeacher(teacherId, pageable);
        return ResponseEntity.ok(responses);
    }

    // =================================================================
    // 🚀 NUEVO ENDPOINT PARA EL RESUMEN DEL PROFESOR
    // URL: GET /api/exams/teacher/{teacherId}/results/summary
    // =================================================================
    @GetMapping("/teacher/{teacherId}/results/summary")
    @PreAuthorize("hasRole('TEACHER')") // Solo profesores pueden acceder a sus resúmenes
    public ResponseEntity<Page<ExamSummaryResponse>> getExamSummariesByTeacher(
            @PathVariable Long teacherId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamSummaryResponse> responses = examService.getExamSummariesByTeacher(teacherId, pageable);
        return ResponseEntity.ok(responses);
    }

    // 🛑 ENDPOINT 1: Quizzes disponibles para un estudiante
    @GetMapping("/student/{studentId}/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<ExamResponse>> getAvailableExamsForStudent(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamResponse> responses = examService.getAvailableExamsForStudent(studentId, pageable);
        return ResponseEntity.ok(responses);
    }

    // 🛑 ENDPOINT 2: Historial de quizzes de un estudiante
    @GetMapping("/student/{studentId}/history")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<ExamResponse>> getStudentExamHistory(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamResponse> responses = examService.getStudentExamHistory(studentId, pageable);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long id,
            @Valid @RequestBody CreateExamRequest examRequest) {
        ExamResponse response = examService.updateExam(id, examRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}