package com.neoeval.backend.dto.response;

import java.io.Serializable;
import java.util.List;

public class QuizReviewResponse implements Serializable {

    private Long resultId;
    private Long examId;
    private String examTitle;
    private Double score;
    private Double maxScore;
    private Integer correctCount;
    private Double percentage;

    // Lista de los detalles de cada pregunta corregida
    private List<QuestionReviewDTO> questions;

    // Constructor (vacío por defecto para frameworks)
    public QuizReviewResponse() {}

    // Constructor completo
    public QuizReviewResponse(Long resultId, Long examId, String examTitle, Double score, Double maxScore, Integer correctCount, Double percentage, List<QuestionReviewDTO> questions) {
        this.resultId = resultId;
        this.examId = examId;
        this.examTitle = examTitle;
        this.score = score;
        this.maxScore = maxScore;
        this.correctCount = correctCount;
        this.percentage = percentage;
        this.questions = questions;
    }

    // Getters y Setters
    public Long getResultId() { return resultId; }
    public void setResultId(Long resultId) { this.resultId = resultId; }
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Double getMaxScore() { return maxScore; }
    public void setMaxScore(Double maxScore) { this.maxScore = maxScore; }
    public Integer getCorrectCount() { return correctCount; }
    public void setCorrectCount(Integer correctCount) { this.correctCount = correctCount; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public List<QuestionReviewDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionReviewDTO> questions) { this.questions = questions; }
}