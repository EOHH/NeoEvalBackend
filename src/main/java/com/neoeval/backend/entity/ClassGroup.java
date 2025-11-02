package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant; // 🟢 CAMBIO CLAVE
import java.util.*;

@Entity
@Table(name = "class_groups")
public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String educationalLevel;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt; // 🟢 CAMBIO CLAVE: Usar Instant

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany(mappedBy = "classGroups")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exam> exams = new HashSet<>();

    // 🟢 NUEVA RELACIÓN INVERSA
    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseModule> courseModules = new HashSet<>();

    @ManyToMany(mappedBy = "classGroups")
    private List<Subject> subjects = new ArrayList<>();

    // 🟢 Constructor
    public ClassGroup() {
        this.createdAt = Instant.now(); // 🟢 Usar Instant
    }

    public ClassGroup(String name, Teacher teacher) {
        this();
        this.name = name;
        this.teacher = teacher;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEducationalLevel() { return educationalLevel; }
    public void setEducationalLevel(String educationalLevel) { this.educationalLevel = educationalLevel; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // 🟢 CAMBIADO: Usar Instant
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public Set<Student> getStudents() { return students; }
    public void setStudents(Set<Student> students) { this.students = students; }
    public Set<Exam> getExams() { return exams; }
    public void setExams(Set<Exam> exams) { this.exams = exams; }

    // 🟢 NUEVO Getter y Setter
    public Set<CourseModule> getCourseModules() { return courseModules; }
    public void setCourseModules(Set<CourseModule> courseModules) { this.courseModules = courseModules; }

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }

    // Helpers
    public void addStudent(Student student) {
        this.students.add(student);
        student.getClassGroups().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getClassGroups().remove(this);
    }

    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setClassGroup(this);
    }
}