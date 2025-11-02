package com.neoeval.backend.dto.response;

import java.io.Serializable;

public class QuestionReviewDTO implements Serializable {

    private Long questionId;
    private String questionText;
    private String questionType;
    private Double maxPoints;       // ⬅️ CAMBIO: Puntos máximos posibles para la pregunta

    // Información de la Corrección
    private String userAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
    private String explanation;
    private Double obtainedScore;    // ⬅️ NUEVO CAMPO: Puntos obtenidos por el estudiante

    // Constructor (vacío por defecto para frameworks)
    public QuestionReviewDTO() {}

    // Constructor completo (Ajustado)
    public QuestionReviewDTO(Long questionId, String questionText, String questionType, Double maxPoints, String userAnswer, String correctAnswer, Boolean isCorrect, String explanation, Double obtainedScore) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.maxPoints = maxPoints;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.explanation = explanation;
        this.obtainedScore = obtainedScore; // Nuevo campo
    }

    // Getters y Setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    // ⬇️ GETTER/SETTER PARA MAX_POINTS (Antes 'points')
    public Double getMaxPoints() { return maxPoints; }
    public void setMaxPoints(Double maxPoints) { this.maxPoints = maxPoints; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    // ⬇️ GETTER/SETTER PARA OBTAINED_SCORE (Nuevo campo)
    public Double getObtainedScore() { return obtainedScore; }
    public void setObtainedScore(Double obtainedScore) { this.obtainedScore = obtainedScore; }
}