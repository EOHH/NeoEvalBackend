package com.neoeval.backend.dto.response;

import java.time.LocalDateTime;

public class StudentResultResponse {

    private Long id;

    // Información básica del Examen
    private Long examId;
    private String examTitle;
    private String subjectName; // Asumo que el Subject es útil

    // Métricas del Resultado
    private Double score;
    private Double percentage;
    private Integer correctAnswers;
    private Integer totalQuestions;

    // Fecha de finalización (Usamos LocalDateTime como en la entidad)
    private LocalDateTime completedAt;

    // ---------------------------
    // 🟢 Getters y Setters
    // ---------------------------
    // (Asegúrate de implementar todos los Getters y Setters aquí)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }

    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}