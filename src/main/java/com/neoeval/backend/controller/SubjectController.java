package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.CreateSubjectRequest;
import com.neoeval.backend.dto.response.SubjectResponse;
import com.neoeval.backend.entity.Subject;
import com.neoeval.backend.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Endpoints existentes...
    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody CreateSubjectRequest request) {
        return ResponseEntity.ok(subjectService.createSubject(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody CreateSubjectRequest request) {
        return ResponseEntity.ok(subjectService.updateSubject(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectService.getSubjectsByTeacher(teacherId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(subjectService.getSubjectsByStudent(studentId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SubjectResponse>> searchSubjects(@RequestParam String q) {
        return ResponseEntity.ok(subjectService.searchSubjects(q));
    }

    @PostMapping("/{subjectId}/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> assignTeacherToSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectService.assignTeacherToSubject(subjectId, teacherId));
    }

    @DeleteMapping("/{subjectId}/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeTeacherFromSubject(
            @PathVariable Long subjectId,
            @PathVariable Long teacherId) {
        subjectService.removeTeacherFromSubject(subjectId, teacherId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Nuevos endpoints para activar/desactivar
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> activateSubject(@PathVariable Long id) {
        // Implementar lógica de activación si es necesario
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> deactivateSubject(@PathVariable Long id) {
        // Implementar lógica de desactivación si es necesario
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }
}