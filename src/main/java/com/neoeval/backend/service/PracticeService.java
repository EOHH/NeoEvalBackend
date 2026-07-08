package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.PracticeCategoryRequest;
import com.neoeval.backend.dto.request.PracticeExerciseRequest;
import com.neoeval.backend.dto.response.PracticeCategoryResponse;
import com.neoeval.backend.dto.response.PracticeExerciseResponse;
import com.neoeval.backend.entity.enums.DifficultyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PracticeService {
    Page<PracticeCategoryResponse> getActiveCategories(Pageable pageable);
    
    PracticeCategoryResponse createCategory(PracticeCategoryRequest request);

    PracticeCategoryResponse updateCategory(Long id, PracticeCategoryRequest request);

    void deleteCategory(Long id);

    PracticeExerciseResponse createExercise(PracticeExerciseRequest request);

    Page<PracticeExerciseResponse> getExercisesByCategoryAndDifficulty(Long categoryId, DifficultyLevel difficultyLevel, Pageable pageable);

    Page<PracticeExerciseResponse> getAllExercises(Pageable pageable);

    PracticeExerciseResponse updateExercise(Long id, PracticeExerciseRequest request);

    void deleteExercise(Long id);
}
