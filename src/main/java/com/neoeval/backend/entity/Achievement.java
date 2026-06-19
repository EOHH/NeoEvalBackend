package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "required_points", nullable = false)
    private Integer requiredPoints;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<StudentAchievement> studentAchievements = new HashSet<>();

    // Constructors
    public Achievement() {
    }

    public Achievement(String name, Integer requiredPoints) {
        this.name = name;
        this.requiredPoints = requiredPoints;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public Set<StudentAchievement> getStudentAchievements() {
        return studentAchievements;
    }

    public void setStudentAchievements(Set<StudentAchievement> studentAchievements) {
        this.studentAchievements = studentAchievements;
    }

    // Helper method
    public void addStudentAchievement(StudentAchievement studentAchievement) {
        studentAchievements.add(studentAchievement);
        studentAchievement.setAchievement(this);
    }
}