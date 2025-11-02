package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;

public class UpdateResultScoreRequest {

    // ✅ La nueva puntuación general del examen (ej. 18.5 de 20.0)
    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score cannot be negative")
    private Double newScore;

    // ✅ El número actualizado de respuestas correctas
    @NotNull(message = "Correct answers count is required")
    @Min(value = 0, message = "Correct answers count cannot be negative")
    private Integer newCorrectAnswers;

    // ✅ NEW: ID de la pregunta que se está calificando / modificando
    @NotNull(message = "Question ID is required")
    @Min(value = 1, message = "Invalid question ID")
    private Long questionId;

    // ✅ NEW: Nueva puntuación para esa pregunta específica
    @NotNull(message = "Question score is required")
    @Min(value = 0, message = "Question score cannot be negative")
    private Double questionScore;

    // ----------------------------------------------------
    //  Constructors
    // ----------------------------------------------------
    public UpdateResultScoreRequest() {
    }

    public UpdateResultScoreRequest(Double newScore, Integer newCorrectAnswers, Long questionId, Double questionScore) {
        this.newScore = newScore;
        this.newCorrectAnswers = newCorrectAnswers;
        this.questionId = questionId;
        this.questionScore = questionScore;
    }

    // ----------------------------------------------------
    //  Getters & Setters
    // ----------------------------------------------------
    public Double getNewScore() {
        return newScore;
    }

    public void setNewScore(Double newScore) {
        this.newScore = newScore;
    }

    public Integer getNewCorrectAnswers() {
        return newCorrectAnswers;
    }

    public void setNewCorrectAnswers(Integer newCorrectAnswers) {
        this.newCorrectAnswers = newCorrectAnswers;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Double getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Double questionScore) {
        this.questionScore = questionScore;
    }
}
