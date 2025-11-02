package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StudentAchievementRequest {

    // El ID del estudiante que obtiene el logro
    @NotNull(message = "The student ID is required")
    @Positive(message = "The student ID must be a positive number")
    private Long studentId;

    // El ID del logro que se otorga
    @NotNull(message = "The achievement ID is required")
    @Positive(message = "The achievement ID must be a positive number")
    private Long achievementId;

    // ---------------------------
    // Constructores
    // ---------------------------
    public StudentAchievementRequest() {
    }

    public StudentAchievementRequest(Long studentId, Long achievementId) {
        this.studentId = studentId;
        this.achievementId = achievementId;
    }

    // ---------------------------
    // Getters y Setters
    // ---------------------------
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Long achievementId) {
        this.achievementId = achievementId;
    }
}