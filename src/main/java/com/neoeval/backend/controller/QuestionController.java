package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.QuestionRequest;
import com.neoeval.backend.dto.response.QuestionResponse;
import com.neoeval.backend.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.neoeval.backend.service.FileStorageService;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final FileStorageService fileStorageService;

    public QuestionController(QuestionService questionService, FileStorageService fileStorageService) {
        this.questionService = questionService;
        this.fileStorageService = fileStorageService;
    }

    // CREATE
    @PostMapping("/exam/{examId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> createQuestion(
            @PathVariable Long examId,
            @Valid @RequestBody QuestionRequest questionRequest) {
        QuestionResponse response = questionService.createQuestion(examId, questionRequest);
        // HttpStatus.CREATED (201) es lo mejor para creación.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Long id) {
        // Si el service lanza ResourceNotFoundException, Spring automáticamente
        // devolverá un 404, pero el cuerpo puede ser un objeto JSON de error.
        QuestionResponse response = questionService.getQuestionById(id);
        return ResponseEntity.ok(response);
    }

    // GET BY EXAM ID (El endpoint problemático que espera una LISTA)
    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByExam(@PathVariable Long examId) {
        List<QuestionResponse> responses = questionService.getQuestionsByExamId(examId);

        // ** OPCIONAL PERO RECOMENDADO: Asegurarse de devolver una lista explícita **
        // En teoría, questionService ya devuelve una lista. Esto es solo una validación extra.
        if (responses == null) {
            // Si por alguna razón el servicio devuelve null, devolvemos una lista vacía.
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Si la lista está vacía, se serializa como [], lo que Flutter espera.
        // Si tiene contenido, se serializa como [{}, {}], lo que Flutter espera.
        return ResponseEntity.ok(responses);
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest questionRequest) {
        QuestionResponse response = questionService.updateQuestion(id, questionRequest);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // GET ALL
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        List<QuestionResponse> responses = questionService.getAllQuestions();
        return ResponseEntity.ok(responses);
    }

    // UPLOAD IMAGE
    @PostMapping("/upload-image")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadQuestionImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageId = fileStorageService.saveFile(file);
            return ResponseEntity.ok(Map.of("imageId", imageId));
        } catch (java.io.IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}