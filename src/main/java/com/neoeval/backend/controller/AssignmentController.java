package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.AssignExamRequest; // ¡Usar este DTO!
import com.neoeval.backend.dto.request.SubmitAnswerRequest;
import com.neoeval.backend.dto.response.AssignmentResponse;
import com.neoeval.backend.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id) {
        AssignmentResponse response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByStudent(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AssignmentResponse> responses = assignmentService.getAssignmentsByStudentId(studentId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByExam(
            @PathVariable Long examId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AssignmentResponse> responses = assignmentService.getAssignmentsByExamId(examId, pageable);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{assignmentId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AssignmentResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody List<SubmitAnswerRequest> answers) {
        AssignmentResponse response = assignmentService.submitAssignment(assignmentId, answers);
        return ResponseEntity.ok(response);
    }

    // Endpoint para asignar un examen a múltiples estudiantes (usando tu AssignExamRequest)
    @PostMapping("/assign-exam") // Una URL más descriptiva
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<AssignmentResponse>> assignExamToStudents(@Valid @RequestBody AssignExamRequest request) {
        List<AssignmentResponse> responses = assignmentService.assignExamToStudents(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses); // Retorna una lista de 201 Created
    }

    @PatchMapping("/{assignmentId}/grade")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<AssignmentResponse> gradeAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Double score) {
        AssignmentResponse response = assignmentService.gradeAssignment(assignmentId, score);
        return ResponseEntity.ok(response);
    }
}