package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.User;
import java.time.LocalDate;

public class StudentResponse extends UserResponse {
    private String educationalLevel;
    private LocalDate birthDate;
    private Integer totalPoints;
    private Integer gamificationLevel;
    private Long parentId;
    private String parentName;
    private Integer examCompleted;
    private Integer certificatesEarned;

    // ✅ Constructor que recibe User (llama al constructor del padre)
    public StudentResponse(User user) {
        super(user);
    }

    // Getters y Setters
    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getGamificationLevel() {
        return gamificationLevel;
    }

    public void setGamificationLevel(Integer gamificationLevel) {
        this.gamificationLevel = gamificationLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getExamCompleted() {
        return examCompleted;
    }

    public void setExamCompleted(Integer examCompleted) {
        this.examCompleted = examCompleted;
    }

    public Integer getCertificatesEarned() {
        return certificatesEarned;
    }

    public void setCertificatesEarned(Integer certificatesEarned) {
        this.certificatesEarned = certificatesEarned;
    }
}
