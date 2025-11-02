package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "student_achievements")
public class StudentAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Fecha y hora del logro (UTC)
    @Column(name = "achievement_date", nullable = false)
    private LocalDateTime achievementDate;

    // ✅ Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    // ---------------------------
    // 🔧 Constructores
    // ---------------------------
    public StudentAchievement() {
        // Se establece la hora actual en UTC al crear el registro
        this.achievementDate = LocalDateTime.now(ZoneOffset.UTC);
    }

    public StudentAchievement(Student student, Achievement achievement) {
        this(); // Usa el constructor anterior (UTC)
        this.student = student;
        this.achievement = achievement;
    }

    // ---------------------------
    // ✅ Getters y Setters
    // ---------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getAchievementDate() { return achievementDate; }
    public void setAchievementDate(LocalDateTime achievementDate) {
        // Si la fecha es nula o viene sin zona, la normalizamos a UTC
        this.achievementDate = achievementDate != null ? achievementDate : LocalDateTime.now(ZoneOffset.UTC);
    }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Achievement getAchievement() { return achievement; }
    public void setAchievement(Achievement achievement) { this.achievement = achievement; }
}
