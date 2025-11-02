package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; // Permite 0 o números positivos

public class PointsUpdateRequest {

    @NotNull(message = "Points to add is mandatory")
    @PositiveOrZero(message = "Points must be zero or a positive number")
    private Double pointsToAdd;

    // Getters y Setters
    public Double getPointsToAdd() {
        return pointsToAdd;
    }

    public void setPointsToAdd(Double pointsToAdd) {
        this.pointsToAdd = pointsToAdd;
    }
}