package com.neoeval.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String expertiseArea;

    @ManyToMany(mappedBy = "teachers")
    @BatchSize(size = 20)
    private Set<Subject> subjects = new HashSet<>();

    // 🚀 NUEVA RELACIÓN: Un profesor gestiona muchos módulos de curso
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<CourseModule> courseModules = new ArrayList<>();


    // Constructores
    public Teacher() {
        super();
        this.setUserType("TEACHER");
    }

    public Teacher(String name, String email, String password) {
        super(name, email, password, "TEACHER");
    }

    public Teacher(String name, String email, String password, String department, String expertiseArea) {
        super(name, email, password, "TEACHER");
        this.department = department;
        this.expertiseArea = expertiseArea;
    }

    // Getters y Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    // 🔹 NUEVOS Getters y Setters
    public List<CourseModule> getCourseModules() {
        return courseModules;
    }

    public void setCourseModules(List<CourseModule> courseModules) {
        this.courseModules = courseModules;
    }

    // 🔹 Helper method (opcional, pero buena práctica)
    public void addCourseModule(CourseModule courseModule) {
        this.courseModules.add(courseModule);
        if (courseModule.getTeacher() != this) {
            courseModule.setTeacher(this);
        }
    }
}