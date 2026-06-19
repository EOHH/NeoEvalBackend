package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.PracticeCategoryRequest;
import com.neoeval.backend.dto.request.PracticeExerciseRequest;
import com.neoeval.backend.dto.response.PracticeCategoryResponse;
import com.neoeval.backend.dto.response.PracticeExerciseResponse;
import com.neoeval.backend.entity.enums.DifficultyLevel;
import com.neoeval.backend.service.PracticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    @GetMapping("/categories")
    public ResponseEntity<Page<PracticeCategoryResponse>> getActiveCategories(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(practiceService.getActiveCategories(pageable));
    }

    @PostMapping("/categories")
    public ResponseEntity<PracticeCategoryResponse> createCategory(
            @Valid @RequestBody PracticeCategoryRequest request) {
        return new ResponseEntity<>(practiceService.createCategory(request), HttpStatus.CREATED);
    }

    @PostMapping("/exercises")
    public ResponseEntity<PracticeExerciseResponse> createExercise(
            @Valid @RequestBody PracticeExerciseRequest request) {
        return new ResponseEntity<>(practiceService.createExercise(request), HttpStatus.CREATED);
    }

    @GetMapping("/exercises")
    public ResponseEntity<Page<PracticeExerciseResponse>> getExercises(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) DifficultyLevel difficultyLevel,
            @PageableDefault(size = 50) Pageable pageable) {
        
        if (categoryId != null && difficultyLevel != null) {
            return ResponseEntity.ok(practiceService.getExercisesByCategoryAndDifficulty(categoryId, difficultyLevel, pageable));
        }
        
        return ResponseEntity.ok(practiceService.getAllExercises(pageable));
    }

    @PutMapping("/exercises/{id}")
    public ResponseEntity<PracticeExerciseResponse> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody PracticeExerciseRequest request) {
        return ResponseEntity.ok(practiceService.updateExercise(id, request));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        practiceService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
