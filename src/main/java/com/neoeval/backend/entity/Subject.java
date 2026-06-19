package com.neoeval.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "code")
})
@EntityListeners(AuditingEntityListener.class)
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    @Column(length = 20, unique = true)
    private String code; // Ej: "MAT-101", "FIS-202"

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String description;

    @Size(max = 50, message = "El nivel educativo no puede exceder 50 caracteres")
    @Column(length = 50)
    private String educationalLevel;

    private Integer credits; // Créditos académicos (ej: 3, 4, 6)
    private Integer hoursPerWeek; // Horas semanales (ej: 4, 6, 8)

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Semester semester; // SEMESTER_1, SEMESTER_2, ANNUAL, etc.

    @Column(nullable = false)
    private Boolean isActive = true;

    // ✅ Se guardan automáticamente en UTC
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ✅ RELACIÓN CON EXAMS
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<Exam> exams = new ArrayList<>();

    // ✅ RELACIÓN CON TEACHERS (Muchos a Muchos)
    @ManyToMany
    @JoinTable(
            name = "subject_teachers",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @BatchSize(size = 20)
    private List<User> teachers = new ArrayList<>();

    // ✅ RELACIÓN CON CLASS GROUPS (Muchos a Muchos)
    @ManyToMany
    @JoinTable(
            name = "subject_class_groups",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "class_group_id")
    )
    @BatchSize(size = 20)
    private List<ClassGroup> classGroups = new ArrayList<>();

    // 🚀 NUEVA RELACIÓN: Una asignatura tiene muchos Módulos de Curso
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<CourseModule> courseModules = new ArrayList<>();

    // 🔹 Constructores
    public Subject() {}

    public Subject(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Subject(String name, String code, String description, String educationalLevel,
                   Integer credits, Integer hoursPerWeek, Semester semester) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.educationalLevel = educationalLevel;
        this.credits = credits;
        this.hoursPerWeek = hoursPerWeek;
        this.semester = semester;
    }

    // 🔹 Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEducationalLevel() { return educationalLevel; }
    public void setEducationalLevel(String educationalLevel) { this.educationalLevel = educationalLevel; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Integer getHoursPerWeek() { return hoursPerWeek; }
    public void setHoursPerWeek(Integer hoursPerWeek) { this.hoursPerWeek = hoursPerWeek; }

    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<Exam> getExams() { return exams; }
    public void setExams(List<Exam> exams) { this.exams = exams; }

    public List<User> getTeachers() { return teachers; }
    public void setTeachers(List<User> teachers) { this.teachers = teachers; }

    public List<ClassGroup> getClassGroups() { return classGroups; }
    public void setClassGroups(List<ClassGroup> classGroups) { this.classGroups = classGroups; }

    // 🔹 NUEVOS Getters y Setters
    public List<CourseModule> getCourseModules() { return courseModules; }
    public void setCourseModules(List<CourseModule> courseModules) { this.courseModules = courseModules; }

    // 🔹 Helper methods
    public void addExam(Exam exam) {
        exams.add(exam);
        exam.setSubject(this);
    }

    public void removeExam(Exam exam) {
        exams.remove(exam);
        exam.setSubject(null);
    }

    public void addTeacher(User teacher) {
        if (!teachers.contains(teacher)) {
            teachers.add(teacher);
            if (!teacher.getSubjects().contains(this)) {
                // Asumiendo que teacher.getSubjects() es accesible y manejado en Teacher.java/User.java
                // Si Teacher extiende de User y User tiene getSubjects(), esto es correcto.
                // Si la relación ManyToMany se maneja en Teacher, el método addTeacher debería estar en Teacher o User.
                // Por ahora, confiamos en la estructura existente.
                // teacher.getSubjects().add(this);
            }
        }
    }

    public void removeTeacher(User teacher) {
        if (teachers.contains(teacher)) {
            teachers.remove(teacher);
            // teacher.getSubjects().remove(this); // Comentado por la misma razón anterior.
        }
    }

    public void addClassGroup(ClassGroup classGroup) {
        if (!classGroups.contains(classGroup)) {
            classGroups.add(classGroup);
            if (!classGroup.getSubjects().contains(this)) {
                classGroup.getSubjects().add(this);
            }
        }
    }

    public void removeClassGroup(ClassGroup classGroup) {
        if (classGroups.contains(classGroup)) {
            classGroups.remove(classGroup);
            classGroup.getSubjects().remove(this);
        }
    }

    // 🔹 NUEVO Helper method
    public void addCourseModule(CourseModule courseModule) {
        this.courseModules.add(courseModule);
        if (courseModule.getSubject() != this) {
            courseModule.setSubject(this);
        }
    }


    // 🔹 Enum para semestres
    public enum Semester {
        SEMESTER_1,
        SEMESTER_2,
        ANNUAL,
        TRIMESTER_1,
        TRIMESTER_2,
        TRIMESTER_3
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}