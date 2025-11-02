package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class AchievementRequest {

    @NotBlank(message = "Achievement name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters") // Ajusta el tamaño según necesites
    private String description;

    @Size(max = 255, message = "Image URL cannot exceed 255 characters") // Ajusta el tamaño
    private String imageUrl;

    @NotNull(message = "Required points are required")
    @Min(value = 0, message = "Required points cannot be negative")
    private Integer requiredPoints;

    // Constructors (opcional, pero útil)
    public AchievementRequest() {
    }

    public AchievementRequest(String name, String description, String imageUrl, Integer requiredPoints) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.requiredPoints = requiredPoints;
    }

    // Getters and Setters
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
}