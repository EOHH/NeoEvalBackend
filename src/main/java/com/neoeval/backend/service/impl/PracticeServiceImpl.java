package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.PracticeCategoryRequest;
import com.neoeval.backend.dto.request.PracticeExerciseRequest;
import com.neoeval.backend.dto.response.PracticeCategoryResponse;
import com.neoeval.backend.dto.response.PracticeExerciseResponse;
import com.neoeval.backend.entity.PracticeCategory;
import com.neoeval.backend.entity.PracticeExercise;
import com.neoeval.backend.entity.enums.DifficultyLevel;
import com.neoeval.backend.repository.PracticeCategoryRepository;
import com.neoeval.backend.repository.PracticeExerciseRepository;
import com.neoeval.backend.repository.UserRepository;
import com.neoeval.backend.repository.ClassGroupRepository;
import com.neoeval.backend.entity.User;
import com.neoeval.backend.entity.ClassGroup;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.service.PracticeService;
import com.neoeval.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PracticeServiceImpl implements PracticeService {

    private final PracticeCategoryRepository categoryRepository;
    private final PracticeExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ClassGroupRepository classGroupRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PracticeCategoryResponse> getActiveCategories(Pageable pageable) {
        User currentUser = getCurrentUser();

        if (currentUser.getUserType().equals("TEACHER")) {
            return categoryRepository.findByTeacher_Id(currentUser.getId(), pageable)
                    .map(this::mapToCategoryResponse);
        } else if (currentUser.getUserType().equals("STUDENT")) {
            if (currentUser instanceof Student) {
                Student student = (Student) currentUser;
                if (!student.getClassGroups().isEmpty()) {
                    Long groupId = student.getClassGroups().iterator().next().getId();
                    return categoryRepository.findByClassGroup_IdAndActiveTrue(groupId, pageable)
                            .map(this::mapToCategoryResponse);
                }
            }
        }
        return categoryRepository.findByActiveTrue(pageable).map(this::mapToCategoryResponse);
    }

    @Override
    @Transactional
    public PracticeCategoryResponse createCategory(PracticeCategoryRequest request) {
        User currentUser = getCurrentUser();
        ClassGroup group = classGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));

        PracticeCategory category = PracticeCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(request.isActive())
                .teacher(currentUser)
                .classGroup(group)
                .build();
        
        return mapToCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public PracticeExerciseResponse createExercise(PracticeExerciseRequest request) {
        PracticeCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getCategoryId()));

        PracticeExercise exercise = PracticeExercise.builder()
                .category(category)
                .difficultyLevel(request.getDifficultyLevel())
                .questionType(request.getQuestionType())
                .content(request.getContent())
                .options(request.getOptions())
                .correctAnswer(request.getCorrectAnswer())
                .imageUrl(request.getImageUrl())
                .build();

        return mapToExerciseResponse(exerciseRepository.save(exercise));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PracticeExerciseResponse> getExercisesByCategoryAndDifficulty(Long categoryId, DifficultyLevel difficultyLevel, Pageable pageable) {
        return exerciseRepository.findByCategory_IdAndDifficultyLevel(categoryId, difficultyLevel, pageable)
                .map(this::mapToExerciseResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PracticeExerciseResponse> getAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable)
                .map(this::mapToExerciseResponse);
    }

    @Override
    @Transactional
    public PracticeExerciseResponse updateExercise(Long id, PracticeExerciseRequest request) {
        PracticeExercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));

        PracticeCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));

        exercise.setCategory(category);
        exercise.setDifficultyLevel(request.getDifficultyLevel());
        exercise.setQuestionType(request.getQuestionType());
        exercise.setContent(request.getContent());
        exercise.setOptions(request.getOptions());
        exercise.setCorrectAnswer(request.getCorrectAnswer());
        exercise.setImageUrl(request.getImageUrl());

        PracticeExercise updatedExercise = exerciseRepository.save(exercise);
        return mapToExerciseResponse(updatedExercise);
    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        PracticeExercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado"));
        exerciseRepository.delete(exercise);
    }

    private PracticeCategoryResponse mapToCategoryResponse(PracticeCategory category) {
        return PracticeCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .groupId(category.getClassGroup() != null ? category.getClassGroup().getId() : null)
                .groupName(category.getClassGroup() != null ? category.getClassGroup().getName() : null)
                .teacherId(category.getTeacher() != null ? category.getTeacher().getId() : null)
                .build();
    }

    private PracticeExerciseResponse mapToExerciseResponse(PracticeExercise exercise) {
        return PracticeExerciseResponse.builder()
                .id(exercise.getId())
                .categoryId(exercise.getCategory().getId())
                .categoryName(exercise.getCategory().getName())
                .difficultyLevel(exercise.getDifficultyLevel())
                .questionType(exercise.getQuestionType())
                .content(exercise.getContent())
                .options(exercise.getOptions())
                .correctAnswer(exercise.getCorrectAnswer())
                .imageUrl(exercise.getImageUrl())
                .build();
    }
}
