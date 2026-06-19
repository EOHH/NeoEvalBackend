package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.hibernate.annotations.BatchSize;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id") // Correcto para herencia JOINED
public class Student extends User {

    @Column(name = "educational_level", length = 50)
    private String educationalLevel;

    // ✅ Fecha de nacimiento (solo fecha, sin hora)
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "total_points")
    private Integer totalPoints = 0;

    @Column(name = "gamification_level")
    private Integer gamificationLevel = 1;

    // ✅ Relaciones
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_class_groups",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "class_group_id")
    )
    @BatchSize(size = 20)
    private Set<ClassGroup> classGroups = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Certificate> certificates = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<StudentAchievement> achievements = new HashSet<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Parent parent;

    // ---------------------------
    // 🔧 Constructores
    // ---------------------------
    public Student() {
        super();
        setUserType("STUDENT");
    }

    public Student(String name, String email, String password) {
        super(name, email, password, "STUDENT");
    }

    // ---------------------------
    // ✅ Getters y Setters
    // ---------------------------
    public String getEducationalLevel() { return educationalLevel; }
    public void setEducationalLevel(String educationalLevel) { this.educationalLevel = educationalLevel; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Integer getGamificationLevel() { return gamificationLevel; }
    public void setGamificationLevel(Integer gamificationLevel) { this.gamificationLevel = gamificationLevel; }

    public Set<ClassGroup> getClassGroups() { return classGroups; }
    public void setClassGroups(Set<ClassGroup> classGroups) { this.classGroups = classGroups; }

    public Set<Assignment> getAssignments() { return assignments; }
    public void setAssignments(Set<Assignment> assignments) { this.assignments = assignments; }

    public Set<Certificate> getCertificates() { return certificates; }
    public void setCertificates(Set<Certificate> certificates) { this.certificates = certificates; }

    public Set<StudentAchievement> getAchievements() { return achievements; }
    public void setAchievements(Set<StudentAchievement> achievements) { this.achievements = achievements; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) {
        this.parent = parent;
        if (parent != null && (parent.getStudent() == null || !parent.getStudent().equals(this))) {
            parent.setStudent(this);
        }
    }

    // ---------------------------
    // 🧩 Helper methods
    // ---------------------------

    /**
     * Añade la puntuación obtenida en un quiz al total acumulado del estudiante.
     * @param points La puntuación obtenida en el quiz.
     */
    public void addPoints(double points) {
        // Redondeamos los puntos a entero y los sumamos al total.
        int newPoints = (int) Math.round(points);
        this.totalPoints += newPoints;
    }

    public void addClassGroup(ClassGroup classGroup) {
        this.classGroups.add(classGroup);
        classGroup.getStudents().add(this);
    }

    public void removeClassGroup(ClassGroup classGroup) {
        this.classGroups.remove(classGroup);
        classGroup.getStudents().remove(this);
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setStudent(this);
    }

    public void addCertificate(Certificate certificate) {
        this.certificates.add(certificate);
        certificate.setStudent(this);
    }

    public void addAchievement(StudentAchievement achievement) {
        this.achievements.add(achievement);
        achievement.setStudent(this);
    }
}