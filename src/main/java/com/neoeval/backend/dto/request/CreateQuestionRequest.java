package com.neoeval.backend.dto.request;

// This DTO seems to be a placeholder or empty.
// If it's intended to be used directly, it should have fields.
// Assuming it's meant to represent a new question, it would likely be similar to QuestionRequest.
// However, if the CreateExamRequest's 'questions' list uses QuestionRequest, then
// a separate CreateQuestionRequest might not be strictly necessary if questions
// are only created as part of an exam. If questions can be created independently,
// then this DTO would be filled with similar fields as QuestionRequest.

// If questions are *only* created as part of an exam (via CreateExamRequest),
// then this DTO can be removed. If questions can be created independently,
// it should be fully defined.

// For now, I'll assume it's used for independent question creation and complete it.
// If it's redundant, you can remove it.

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

public class CreateQuestionRequest {
    @NotBlank(message = "Question text is required")
    @Size(max = 2000, message = "Question text cannot exceed 2000 characters") // Increased size for potential long questions
    private String questionText;

    @NotBlank(message = "Question type is required")
    // Consider adding @Pattern if you have a strict enum for questionType
    private String questionType; // MULTIPLE_CHOICE, TRUE_FALSE, OPEN, MATH, DRAG_DROP, MEDIA

    @DecimalMin(value = "1.0", message = "Difficulty must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Difficulty cannot exceed 5.0")
    private Double difficulty; // 1-5 scale

    @NotNull(message = "Points for the question are required")
    @Min(value = 0, message = "Points cannot be negative")
    private Integer points;

    // This should be a flexible JSON string or a more specific DTO if structure is fixed.
    // For simplicity, keeping it as String as in your entities.
    private String options; // JSON string for multiple choice options

    // This should be a flexible JSON string or a more specific DTO.
    private String correctAnswer; // JSON structure varies by question type

    @Size(max = 2000, message = "Explanation cannot exceed 2000 characters")
    private String explanation;

    // Consider if a question can be created independently without being immediately associated with an exam.
    // If it MUST be associated with an exam upon creation, then add @NotNull(message = "Exam ID is required")
    // private Long examId;

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}