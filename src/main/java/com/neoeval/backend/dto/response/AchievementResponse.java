package com.neoeval.backend.dto.response;

public class AchievementResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer requiredPoints;
    private Integer studentsAchieved;

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

    public Integer getStudentsAchieved() {
        return studentsAchieved;
    }

    public void setStudentsAchieved(Integer studentsAchieved) {
        this.studentsAchieved = studentsAchieved;
    }
}