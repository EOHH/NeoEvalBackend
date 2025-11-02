package com.neoeval.backend.controller;

import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.TeacherResponse;
import com.neoeval.backend.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
        List<TeacherResponse> responses = teacherService.getAllTeachers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<ClassGroupResponse>> getGroupsByTeacher(@PathVariable Long id) {
        List<ClassGroupResponse> responses = teacherService.getGroupsByTeacher(id);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/exams")
    public ResponseEntity<List<ExamResponse>> getExamsByTeacher(@PathVariable Long id) {
        List<ExamResponse> responses = teacherService.getExamsByTeacher(id);
        return ResponseEntity.ok(responses);
    }
}