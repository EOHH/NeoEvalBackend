package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

public class QuestionRequest {
    @NotBlank(message = "Question text is required")
    @Size(max = 2000, message = "Question text cannot exceed 2000 characters")
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

    // This is good for flexibility, but parsing/validation will be needed in service layer.
    private String options; // JSON string for multiple choice options

    // This is good for flexibility, but parsing/validation will be needed in service layer.
    private String correctAnswer; // JSON structure varies by question type

    @Size(max = 2000, message = "Explanation cannot exceed 2000 characters")
    private String explanation;

    private String imageId;

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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}