package com.neoeval.backend.dto.response;

import java.time.LocalDateTime;

public class ExamSummaryResponse {
    private Long examId;
    private String title;
    private Double averageScore; // Promedio de score (ej: 18.5)
    private Long submissionsCount; // Cantidad de estudiantes que rindieron
    private LocalDateTime lastSubmission; // Fecha y hora de la última entrega
    private String subjectName; // Campo para el nombre de la materia

    /**
     * Constructor usado por la consulta JPQL de agregación.
     * Se ha añadido 'subjectName' para optimización de una sola consulta.
     */
    public ExamSummaryResponse(Long examId, String title, Double averageScore, Long submissionsCount, LocalDateTime lastSubmission, String subjectName) { // 👈 CONSTRUCTOR MODIFICADO
        this.examId = examId;
        this.title = title;
        this.averageScore = averageScore;
        this.submissionsCount = submissionsCount;
        this.lastSubmission = lastSubmission;
        this.subjectName = subjectName; // 👈 Asignación
    }

    // Constructor vacío
    public ExamSummaryResponse() {}

    // Getters y Setters (se mantienen)
    public Long getExamId() { return examId; }
    public void setId(Long examId) { this.examId = examId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }

    public Long getSubmissionsCount() { return submissionsCount; }
    public void setSubmissionsCount(Long submissionsCount) { this.submissionsCount = submissionsCount; }

    public LocalDateTime getLastSubmission() { return lastSubmission; }
    public void setLastSubmission(LocalDateTime lastSubmission) { this.lastSubmission = lastSubmission; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}