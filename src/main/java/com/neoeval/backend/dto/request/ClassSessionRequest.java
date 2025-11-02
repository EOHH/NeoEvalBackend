package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ClassSessionRequest {

    @NotBlank(message = "El título de la sesión es obligatorio")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    private String title;

    @Size(max = 1000, message = "El objetivo de aprendizaje no puede exceder 1000 caracteres")
    private String learningObjective;

    @NotNull(message = "El índice de orden es obligatorio")
    private Integer orderIndex;

    // Nota: El courseModuleId se obtiene típicamente de la URL (path variable)

    // Getters y Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLearningObjective() { return learningObjective; }
    public void setLearningObjective(String learningObjective) { this.learningObjective = learningObjective; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}