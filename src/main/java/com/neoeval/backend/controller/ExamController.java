package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.CreateExamRequest;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.ExamSummaryResponse; // 👈 ¡NUEVA IMPORTACIÓN!
import com.neoeval.backend.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<ExamResponse>> getExamsByGroup(@PathVariable Long groupId) {
        List<ExamResponse> responses = examService.getExamsByGroup(groupId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ExamResponse>> getExamsByTeacher(@PathVariable Long teacherId) {
        List<ExamResponse> responses = examService.getExamsByTeacher(teacherId);
        return ResponseEntity.ok(responses);
    }

    // =================================================================
    // 🚀 NUEVO ENDPOINT PARA EL RESUMEN DEL PROFESOR
    // URL: GET /api/exams/teacher/{teacherId}/results/summary
    // =================================================================
    @GetMapping("/teacher/{teacherId}/results/summary")
    @PreAuthorize("hasRole('TEACHER')") // Solo profesores pueden acceder a sus resúmenes
    public ResponseEntity<List<ExamSummaryResponse>> getExamSummariesByTeacher(@PathVariable Long teacherId) {
        List<ExamSummaryResponse> responses = examService.getExamSummariesByTeacher(teacherId);
        return ResponseEntity.ok(responses);
    }

    // 🛑 ENDPOINT 1: Quizzes disponibles para un estudiante
    @GetMapping("/student/{studentId}/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ExamResponse>> getAvailableExamsForStudent(@PathVariable Long studentId) {
        List<ExamResponse> responses = examService.getAvailableExamsForStudent(studentId);
        return ResponseEntity.ok(responses);
    }

    // 🛑 ENDPOINT 2: Historial de quizzes de un estudiante
    @GetMapping("/student/{studentId}/history")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ExamResponse>> getStudentExamHistory(@PathVariable Long studentId) {
        List<ExamResponse> responses = examService.getStudentExamHistory(studentId);
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