package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.enums.DifficultyLevel;
import com.neoeval.backend.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PracticeExerciseResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private DifficultyLevel difficultyLevel;
    private QuestionType questionType;
    private String content;
    private String options;
    private String correctAnswer;
}
