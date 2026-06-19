package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "course_modules")
public class CourseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    // ✅ CORREGIDO: Inicialización en la declaración asegura el valor por defecto
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    // 🔗 RELACIÓN M:1 con Subject (La columna 'subject_id' se crea aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // 🔗 RELACIÓN M:1 con Teacher (La columna 'teacher_id' se crea aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    // ✅ NUEVO CAMPO: Relación M:1 con ClassGroup, usando 'group_id' (como en Exam.java)
    // Se permite que sea nulo si el módulo es un borrador o es general.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id") // Se creará la columna group_id en la tabla course_modules
    private ClassGroup classGroup;

    // 🔗 Relación 1:M con ClassSession (Las sesiones temáticas)
    @OneToMany(mappedBy = "courseModule", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @BatchSize(size = 20)
    private List<ClassSession> sessions = new ArrayList<>();

    // Constructores
    public CourseModule() {
        // El campo 'createdAt' ya se inicializa arriba.
        // Solo verificamos que la colección esté inicializada.
        if (this.sessions == null) {
            this.sessions = new ArrayList<>();
        }
    }

    // 🌟 CONSTRUCTOR CORREGIDO: Inicializa explícitamente el campo obligatorio 'createdAt'
    public CourseModule(String title, Subject subject, Teacher teacher, ClassGroup classGroup) {
        this.title = title;
        this.subject = subject;
        this.teacher = teacher;
        this.classGroup = classGroup;
        this.createdAt = Instant.now(); // 👈 ¡SOLUCIÓN APLICADA!
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public ClassGroup getClassGroup() { return classGroup; }
    public void setClassGroup(ClassGroup classGroup) { this.classGroup = classGroup; }
    public List<ClassSession> getSessions() { return sessions; }
    public void setSessions(List<ClassSession> sessions) { this.sessions = sessions; }

    // Helpers
    public void addSession(ClassSession session) {
        sessions.add(session);
        session.setCourseModule(this);
    }
}