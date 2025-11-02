package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "student_results")
public class StudentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // ✅ Campos de resultado
    @Column(nullable = false)
    private Double score; // Puntuación final (ej. 18.5)

    @Column(nullable = false)
    private Double percentage; // Porcentaje de acierto (ej. 92.5)

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "correct_answers", nullable = false)
    private Integer correctAnswers;

    // ✅ Fecha y hora de finalización (UTC)
    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    // ---------------------------
    // 🔧 Constructores
    // ---------------------------
    public StudentResult() {
        // Forzamos a UTC para que se guarde sin desplazamiento
        this.completedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public StudentResult(Student student, Exam exam, Double score, Double percentage,
                         Integer totalQuestions, Integer correctAnswers) {
        this(); // Llama al constructor anterior (UTC)
        this.student = student;
        this.exam = exam;
        this.score = score;
        this.percentage = percentage;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }

    // ---------------------------
    // ✅ Getters y Setters
    // ---------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(Integer correctAnswers) { this.correctAnswers = correctAnswers; }

    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setCompletedAt(LocalDateTime completedAt) {
        // Si viene nulo o sin zona, forzamos UTC
        this.completedAt = completedAt != null ? completedAt : LocalDateTime.now(ZoneOffset.UTC);
    }
}
