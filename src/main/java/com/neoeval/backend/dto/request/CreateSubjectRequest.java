package com.neoeval.backend.dto.request;

import com.neoeval.backend.entity.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSubjectRequest {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String code;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @Size(max = 50, message = "El nivel educativo no puede exceder 50 caracteres")
    private String educationalLevel;

    private Integer credits;

    private Integer hoursPerWeek;

    private Subject.Semester semester;

    // Constructores
    public CreateSubjectRequest() {
    }

    public CreateSubjectRequest(String name, String code, String description,
                                String educationalLevel, Integer credits,
                                Integer hoursPerWeek, Subject.Semester semester) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.educationalLevel = educationalLevel;
        this.credits = credits;
        this.hoursPerWeek = hoursPerWeek;
        this.semester = semester;
    }

    // Getters y Setters
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
}