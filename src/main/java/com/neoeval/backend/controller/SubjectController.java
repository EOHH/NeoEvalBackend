package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.CreateSubjectRequest;
import com.neoeval.backend.dto.response.SubjectResponse;
import com.neoeval.backend.entity.Subject;
import com.neoeval.backend.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<Page<SubjectResponse>> getAllSubjects(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subjectService.getAllSubjects(pageable));
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
    public ResponseEntity<Page<SubjectResponse>> getSubjectsByTeacher(
            @PathVariable Long teacherId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subjectService.getSubjectsByTeacher(teacherId, pageable));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<SubjectResponse>> getSubjectsByStudent(
            @PathVariable Long studentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subjectService.getSubjectsByStudent(studentId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SubjectResponse>> searchSubjects(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subjectService.searchSubjects(q, pageable));
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