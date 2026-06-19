package com.neoeval.backend.controller;

import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.TeacherResponse;
import com.neoeval.backend.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id) {
        TeacherResponse response = teacherService.getTeacherById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TeacherResponse>> getAllTeachers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TeacherResponse> responses = teacherService.getAllTeachers(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<Page<ClassGroupResponse>> getGroupsByTeacher(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ClassGroupResponse> responses = teacherService.getGroupsByTeacher(id, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/exams")
    public ResponseEntity<Page<ExamResponse>> getExamsByTeacher(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ExamResponse> responses = teacherService.getExamsByTeacher(id, pageable);
        return ResponseEntity.ok(responses);
    }
}