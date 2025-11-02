package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant; // ✅ Cambiado de LocalDateTime a Instant

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🟢 CORRECCIÓN: Usamos Instant para la base de datos (UTC)
    @Column(name = "assigned_date", nullable = false)
    private Instant assignedDate;

    @Column(name = "completed")
    private boolean completed = false;

    // 🟢 CORRECCIÓN: Usamos Instant
    @Column(name = "completion_date")
    private Instant completionDate;

    @Column
    private Double score; // Calificación final del intento

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // -------------------------------------------------------------------------
    // 🟢 Constructores
    public Assignment() {
        this.assignedDate = Instant.now(); // ✅ Usamos Instant.now()
    }

    public Assignment(Student student, Exam exam) {
        this();
        this.student = student;
        this.exam = exam;
    }

    // -------------------------------------------------------------------------
    // 🟢 Getters y Setters (actualizados para Instant)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedDate() { // ✅ Devuelve Instant
        return assignedDate;
    }

    public void setAssignedDate(Instant assignedDate) { // ✅ Recibe Instant
        this.assignedDate = assignedDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Instant getCompletionDate() { // ✅ Devuelve Instant
        return completionDate;
    }

    public void setCompletionDate(Instant completionDate) { // ✅ Recibe Instant
        this.completionDate = completionDate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}