package com.neoeval.backend.dto.response;

import java.time.Instant;   // Para createdAt y lastLogin (Heredado de UserResponse)
import java.time.LocalDate; // Para birthDate

public class StudentResponse extends UserResponse {
    private String educationalLevel;
    private LocalDate birthDate; // <-- Tipo actualizado
    private Integer totalPoints;
    private Integer gamificationLevel;
    private Long parentId;
    private String parentName;
    private Integer examCompleted;
    private Integer certificatesEarned;

    // Constructor vacío
    public StudentResponse() {
        super();
    }

    // Constructor completo para facilitar el mapeo
    public StudentResponse(
            Long id,
            String name,
            String email,
            String userType,
            Instant createdAt, // Instant del padre
            Instant lastLogin, // Instant del padre
            boolean active,
            String educationalLevel,
            LocalDate birthDate, // <-- Tipo actualizado
            Integer totalPoints,
            Integer gamificationLevel,
            Long parentId,
            String parentName,
            Integer examCompleted,
            Integer certificatesEarned
    ) {
        // Llama al constructor de la clase padre (UserResponse)
        super(id, name, email, userType, createdAt, lastLogin, active);
        this.educationalLevel = educationalLevel;
        this.birthDate = birthDate;
        this.totalPoints = totalPoints;
        this.gamificationLevel = gamificationLevel;
        this.parentId = parentId;
        this.parentName = parentName;
        this.examCompleted = examCompleted;
        this.certificatesEarned = certificatesEarned;
    }

    // Getters y Setters para los campos específicos de Student

    // Campo: educationalLevel
    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    // Campo: birthDate
    public LocalDate getBirthDate() { // <-- Tipo de retorno actualizado
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) { // <-- Tipo de parámetro actualizado
        this.birthDate = birthDate;
    }

    // Campo: totalPoints
    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    // Campo: gamificationLevel
    public Integer getGamificationLevel() {
        return gamificationLevel;
    }

    public void setGamificationLevel(Integer gamificationLevel) {
        this.gamificationLevel = gamificationLevel;
    }

    // Campo: parentId
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // Campo: parentName
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    // Campo: examCompleted
    public Integer getExamCompleted() {
        return examCompleted;
    }

    public void setExamCompleted(Integer examCompleted) {
        this.examCompleted = examCompleted;
    }

    // Campo: certificatesEarned
    public Integer getCertificatesEarned() {
        return certificatesEarned;
    }

    public void setCertificatesEarned(Integer certificatesEarned) {
        this.certificatesEarned = certificatesEarned;
    }
}