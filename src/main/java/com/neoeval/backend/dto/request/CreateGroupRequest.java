package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // <-- Añadido
import jakarta.validation.constraints.Size;

public class CreateGroupRequest {
    @NotBlank(message = "ClassGroup name is required")
    @Size(max = 100, message = "ClassGroup name cannot exceed 100 characters")
    private String name;

    @Size(max = 50, message = "Educational level cannot exceed 50 characters")
    private String educationalLevel; // ¿Este campo es para el grupo o para los estudiantes en el grupo? Revisa su pertinencia aquí.

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Teacher ID is required") // <-- Añadido
    private Long teacherId; // <-- Añadido

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTeacherId() { // <-- Añadido
        return teacherId;
    }

    public void setTeacherId(Long teacherId) { // <-- Añadido
        this.teacherId = teacherId;
    }
}