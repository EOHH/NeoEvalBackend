package com.neoeval.backend.dto.response;

import java.time.LocalDateTime;
import java.time.Instant;

public class StudentExamResultDetailResponse {

    private Long resultId;
    private Long studentId;
    private String studentName;
    private String studentEmail; // Asumiendo que Student hereda o tiene un campo email
    private Double score;
    private Double percentage;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Instant completedAt;

    // Constructores
    public StudentExamResultDetailResponse() {
    }

    public StudentExamResultDetailResponse(Long resultId, Long studentId, String studentName, String studentEmail,
                                           Double score, Double percentage, Integer totalQuestions,
                                           Integer correctAnswers, Instant completedAt) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.score = score;
        this.percentage = percentage;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.completedAt = completedAt;
    }

    // Getters y Setters
    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
