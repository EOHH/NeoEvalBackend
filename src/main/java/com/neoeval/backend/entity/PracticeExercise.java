package com.neoeval.backend.entity;

import com.neoeval.backend.entity.enums.DifficultyLevel;
import com.neoeval.backend.entity.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "practice_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PracticeCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 20)
    private QuestionType questionType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(name = "correct_answer", columnDefinition = "TEXT", nullable = false)
    private String correctAnswer;
}
