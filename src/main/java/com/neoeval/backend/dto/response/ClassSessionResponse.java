package com.neoeval.backend.dto.response;

import java.time.Instant;
import java.util.List;

public class ClassSessionResponse {

    private Long id;
    private String title;
    private Integer orderIndex;
    private String learningObjective;
    private Instant createdAt;

    // Relación jerárquica: Los recursos dentro de la sesión
    private List<MaterialResourceResponse> resources;

    // Nota: No incluimos el courseModuleId aquí, ya que este DTO se verá como parte de un CourseModuleResponse

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public String getLearningObjective() { return learningObjective; }
    public void setLearningObjective(String learningObjective) { this.learningObjective = learningObjective; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public List<MaterialResourceResponse> getResources() { return resources; }
    public void setResources(List<MaterialResourceResponse> resources) { this.resources = resources; }
}