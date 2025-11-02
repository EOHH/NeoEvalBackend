package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.StudentAchievementRequest;
import com.neoeval.backend.dto.response.StudentAchievementResponse;
import com.neoeval.backend.service.StudentAchievementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-achievements")
public class StudentAchievementController {

    private final StudentAchievementService studentAchievementService;

    public StudentAchievementController(StudentAchievementService studentAchievementService) {
        this.studentAchievementService = studentAchievementService;
    }

    // 1. Otorgar un logro manualmente (POST) - Ahora devuelve un DTO ligero
    @PostMapping("/award")
    public ResponseEntity<StudentAchievementResponse> awardAchievement( // 👈 Firma actualizada
            @Valid @RequestBody StudentAchievementRequest request) {

        StudentAchievementResponse response = studentAchievementService.awardAchievement(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Obtener todos los logros de un estudiante (GET) - Ahora devuelve una lista de DTOs ligeros
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentAchievementResponse>> getAchievementsByStudent(
              @PathVariable Long studentId) {

        List<StudentAchievementResponse> achievements =
                studentAchievementService.getAchievementsByStudent(studentId);

        return ResponseEntity.ok(achievements);
    }
}