package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación a la pregunta respondida
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Relación al resultado principal (StudentResult)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_result_id", nullable = false)
    private StudentResult studentResult;

    // La respuesta enviada por el estudiante (el String que recibimos en el QuizSubmissionRequest)
    @Column(name = "submitted_answer", columnDefinition = "TEXT")
    private String submittedAnswer;

    // Campo de calificación (puede ser nulo si la calificación es manual/pendiente)
    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "points_awarded")
    private Double pointsAwarded;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();


    // -------------------------------------------------------------------------
    // Constructors
    public StudentAnswer() {
    }

    public StudentAnswer(Question question, StudentResult studentResult, String submittedAnswer, Boolean isCorrect, Double pointsAwarded) {
        this.question = question;
        this.studentResult = studentResult;
        this.submittedAnswer = submittedAnswer;
        this.isCorrect = isCorrect;
        this.pointsAwarded = pointsAwarded;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public StudentResult getStudentResult() { return studentResult; }
    public void setStudentResult(StudentResult studentResult) { this.studentResult = studentResult; }
    public String getSubmittedAnswer() { return submittedAnswer; }
    public void setSubmittedAnswer(String submittedAnswer) { this.submittedAnswer = submittedAnswer; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
    public Double getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(Double pointsAwarded) { this.pointsAwarded = pointsAwarded; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}