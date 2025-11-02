package com.neoeval.backend.controller;

import com.neoeval.backend.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // Endpoint: GET /progress/student/{studentId}/metrics (Progreso)
    // Usado por el frontend para la pantalla de Progreso
    @GetMapping("/student/{studentId}/metrics")
    public ResponseEntity<Map<String, Object>> getStudentProgressMetrics(@PathVariable Long studentId) {
        Map<String, Object> metrics = progressService.getStudentProgressMetrics(studentId);
        return ResponseEntity.ok(metrics);
    }
}