package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class CreateExamRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Exam type is required")
    private String examType; // EXAM, HOMEWORK, PRACTICE

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private Long groupId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    // 🚀 RECOMENDADO: Usamos LocalDateTime para fechas/horas de eventos globales
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;

    private Integer timeLimitMinutes;
    private Integer allowedAttempts;
    private Double averageDifficulty;

    private List<QuestionRequest> questions;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    // 🚀 Getters y Setters actualizados a LocalDateTime
    public LocalDateTime getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDateTime openingDate) { this.openingDate = openingDate; }
    public LocalDateTime getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDateTime closingDate) { this.closingDate = closingDate; }

    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
    public Integer getAllowedAttempts() { return allowedAttempts; }
    public void setAllowedAttempts(Integer allowedAttempts) { this.allowedAttempts = allowedAttempts; }
    public Double getAverageDifficulty() { return averageDifficulty; }
    public void setAverageDifficulty(Double averageDifficulty) { this.averageDifficulty = averageDifficulty; }
    public List<QuestionRequest> getQuestions() { return questions; }
    public void setQuestions(List<QuestionRequest> questions) { this.questions = questions; }
}