package com.neoeval.backend.dto.request;

import com.neoeval.backend.entity.enums.DifficultyLevel;
import com.neoeval.backend.entity.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PracticeExerciseRequest {

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    @NotNull(message = "El nivel de dificultad es obligatorio")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "El tipo de pregunta es obligatorio")
    private QuestionType questionType;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;

    private String options;

    @NotBlank(message = "La respuesta correcta es obligatoria")
    private String correctAnswer;

    private String imageUrl;
}
