package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "exam_type", nullable = false, length = 20)
    private String examType; // EXAM, HOMEWORK, PRACTICE

    // 🚀 Cambiado a LocalDateTime para evitar desfases de zona horaria
    @Column(name = "opening_date")
    private LocalDateTime openingDate;

    // 🚀 Cambiado a LocalDateTime para evitar desfases de zona horaria
    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "allowed_attempts")
    private Integer allowedAttempts = 1;

    @Column(name = "average_difficulty")
    private Double averageDifficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ClassGroup classGroup;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Certificate> certificates = new HashSet<>();

    // Constructors
    public Exam() {}

    public Exam(String title, String examType, User teacher, Subject subject, ClassGroup classGroup) {
        this.title = title;
        this.examType = examType;
        this.teacher = teacher;
        this.subject = subject;
        this.classGroup = classGroup;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    // 🚀 Getters y Setters actualizados a LocalDateTime
    public LocalDateTime getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDateTime openingDate) { this.openingDate = openingDate; }

    public LocalDateTime getClosingDate() { return closingDate; }
    public void setClosingDate(LocalDateTime closingDate) { this.closingDate = closingDate; }

    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

    public Integer getAllowedAttempts() { return allowedAttempts; }
    public void setAllowedAttempts(Integer allowedAttempts) { this.allowedAttempts = allowedAttempts; }

    public Double getAverageDifficulty() { return averageDifficulty; }
    public void setAverageDifficulty(Double averageDifficulty) { this.averageDifficulty = averageDifficulty; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public ClassGroup getClassGroup() { return classGroup; }
    public void setClassGroup(ClassGroup classGroup) { this.classGroup = classGroup; }

    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public Set<Assignment> getAssignments() { return assignments; }
    public void setAssignments(Set<Assignment> assignments) { this.assignments = assignments; }

    public Set<Certificate> getCertificates() { return certificates; }
    public void setCertificates(Set<Certificate> certificates) { this.certificates = certificates; }

    // Helpers
    public void addQuestion(Question question) {
        questions.add(question);
        question.setExam(this);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.setExam(this);
    }

    public void addCertificate(Certificate certificate) {
        certificates.add(certificate);
        certificate.setExam(this);
    }
}