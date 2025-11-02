package com.neoeval.backend.dto.response;

import java.time.LocalDateTime;

public class StudentAchievementResponse {

    private Long id;
    private LocalDateTime achievementDate;
    private Long studentId;       // Solo el ID del estudiante
    private String studentName;   // Información útil del estudiante
    private Long achievementId;   // Solo el ID del logro
    private String achievementName; // Información útil del logro

    // Constructor vacío
    public StudentAchievementResponse() {}

    // Getters y Setters (Debes agregarlos aquí)
    // ...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getAchievementDate() { return achievementDate; }
    public void setAchievementDate(LocalDateTime achievementDate) { this.achievementDate = achievementDate; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Long getAchievementId() { return achievementId; }
    public void setAchievementId(Long achievementId) { this.achievementId = achievementId; }
    public String getAchievementName() { return achievementName; }
    public void setAchievementName(String achievementName) { this.achievementName = achievementName; }
}