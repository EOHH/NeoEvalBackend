package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class StudentIdsRequest {

    // El nombre de la propiedad debe coincidir con la clave JSON que envía Flutter
    @NotEmpty(message = "La lista de IDs de estudiantes no puede estar vacía")
    private List<Long> studentIds;

    // Getter y Setter
    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }
}