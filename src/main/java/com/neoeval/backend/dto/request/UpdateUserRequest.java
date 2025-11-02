package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import java.time.LocalDate; // <-- ¡Nuevo import!

public class UpdateUserRequest {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password; // Puede ser nulo si no se actualiza

    private Boolean active;

    // Estos campos serán usados condicionalmente basado en el userType del usuario que se está actualizando
    @Size(max = 50, message = "Educational level cannot exceed 50 characters")
    private String educationalLevel; // Para estudiantes

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate; // <-- ¡Cambiado a LocalDate!

    @Size(max = 50, message = "Relationship cannot exceed 50 characters")
    private String relationship; // Para padres

    private String department; // Para profesores
    private String subjectTaught; // Para profesores

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isActive() { // Getter para el campo 'active'
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEducationalLevel() {
        return educationalLevel;
    }

    public void setEducationalLevel(String educationalLevel) {
        this.educationalLevel = educationalLevel;
    }

    public LocalDate getBirthDate() { // <-- Cambio de tipo de retorno
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) { // <-- Cambio de tipo de parámetro
        this.birthDate = birthDate;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubjectTaught() {
        return subjectTaught;
    }

    public void setSubjectTaught(String subjectTaught) {
        this.subjectTaught = subjectTaught;
    }
}