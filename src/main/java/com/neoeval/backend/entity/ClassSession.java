package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "class_sessions")
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "learning_objective", length = 1000)
    private String learningObjective;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // 🔗 RELACIÓN M:1 con CourseModule (La columna 'module_id' se crea aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule courseModule;

    // 🔗 Relación 1:M con MaterialResource (Los archivos/enlaces adjuntos)
    @OneToMany(mappedBy = "classSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<MaterialResource> resources = new ArrayList<>();

    // Constructores
    public ClassSession() {}

    public ClassSession(String title, Integer orderIndex, CourseModule courseModule) {
        this.title = title;
        this.orderIndex = orderIndex;
        this.courseModule = courseModule;
    }

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
    public CourseModule getCourseModule() { return courseModule; }
    public void setCourseModule(CourseModule courseModule) { this.courseModule = courseModule; }
    public List<MaterialResource> getResources() { return resources; }
    public void setResources(List<MaterialResource> resources) { this.resources = resources; }

    // Helpers
    public void addResource(MaterialResource resource) {
        resources.add(resource);
        resource.setClassSession(this);
    }
}