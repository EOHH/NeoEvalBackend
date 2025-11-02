package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.Subject;
import java.time.Instant; // ✅ CAMBIADO a Instant

public class SubjectResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String educationalLevel;
    private Integer credits;
    private Integer hoursPerWeek;
    private Subject.Semester semester;
    private Boolean isActive;
    private Instant createdAt; // ✅ CAMBIADO
    private Instant updatedAt; // ✅ CAMBIADO

    // Constructores
    public SubjectResponse() {}

    public SubjectResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubjectResponse(Long id, String name, String code, String description,
                           String educationalLevel, Integer credits, Integer hoursPerWeek,
                           Subject.Semester semester, Boolean isActive,
                           Instant createdAt, Instant updatedAt) { // ✅ CAMBIADO en constructor
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.educationalLevel = educationalLevel;
        this.credits = credits;
        this.hoursPerWeek = hoursPerWeek;
        this.semester = semester;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEducationalLevel() { return educationalLevel; }
    public void setEducationalLevel(String educationalLevel) { this.educationalLevel = educationalLevel; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public Integer getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(Integer hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }
    public Subject.Semester getSemester() { return semester; }
    public void setSemester(Subject.Semester semester) { this.semester = semester; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getCreatedAt() { return createdAt; } // ✅ CAMBIADO
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; } // ✅ CAMBIADO

    public Instant getUpdatedAt() { return updatedAt; } // ✅ CAMBIADO
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; } // ✅ CAMBIADO

    @Override
    public String toString() {
        return "SubjectResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}