package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PracticeCategoryRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;
    
    private boolean active = true;

    @jakarta.validation.constraints.NotNull(message = "El grupo es obligatorio")
    private Long groupId;
}
