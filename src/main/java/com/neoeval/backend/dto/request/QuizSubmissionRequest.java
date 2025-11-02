package com.neoeval.backend.dto.request;

import java.util.List;

public class QuizSubmissionRequest {

    private Long studentId;
    private Long examId;
    private List<AnswerSubmission> answers;

    /**
     * Clase interna para la respuesta de una sola pregunta.
     * MODIFICADO: Ahora usa 'answer' (String) en lugar de 'selectedAnswerId' (Long).
     */
    public static class AnswerSubmission {
        private Long questionId;
        private String answer; // <-- CAMBIO CLAVE

        // Getters y Setters
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }

    // Getters y Setters para QuizSubmissionRequest
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public List<AnswerSubmission> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmission> answers) { this.answers = answers; }
}