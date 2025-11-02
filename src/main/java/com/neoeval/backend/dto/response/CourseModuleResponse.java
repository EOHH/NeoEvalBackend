package com.neoeval.backend.dto.response;

import java.time.Instant;
import java.util.List;

public class CourseModuleResponse {

    private Long id;
    private String title;
    private String description;
    private Instant createdAt;

    // Datos de las entidades relacionadas
    private Long subjectId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private Long classGroupId;
    private String classGroupName;

    // Relación jerárquica: Las sesiones dentro del módulo
    private List<ClassSessionResponse> sessions;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public Long getClassGroupId() { return classGroupId; }
    public void setClassGroupId(Long classGroupId) { this.classGroupId = classGroupId; }
    public String getClassGroupName() { return classGroupName; }
    public void setClassGroupName(String classGroupName) { this.classGroupName = classGroupName; }
    public List<ClassSessionResponse> getSessions() { return sessions; }
    public void setSessions(List<ClassSessionResponse> sessions) { this.sessions = sessions; }
}