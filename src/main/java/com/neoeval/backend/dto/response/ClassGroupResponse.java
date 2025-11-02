package com.neoeval.backend.dto.response;

import java.time.Instant; // ✅ Importado Instant
import java.util.ArrayList;
import java.util.List;
// Importa StudentResponse si no está en el mismo paquete

public class ClassGroupResponse {

    private Long id;
    private String name;
    private String educationalLevel;
    private String description;
    private Instant createdAt; // ✅ Actualizado a Instant
    private Long teacherId;
    private String teacherName;
    private List<StudentResponse> students = new ArrayList<>();
    private Integer studentCount = 0;
    private Integer examCount = 0;

    // Constructor vacío
    public ClassGroupResponse() {
    }

    // Constructor con campos principales (puedes añadir más si es necesario)
    public ClassGroupResponse(Long id, String name, String educationalLevel, String description,
                              Instant createdAt, Long teacherId, String teacherName) {
        this.id = id;
        this.name = name;
        this.educationalLevel = educationalLevel;
        this.description = description;
        this.createdAt = createdAt;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return (name != null) ? name : "Sin nombre"; }
    public void setName(String name) { this.name = name; }

    public String getEducationalLevel() { return (educationalLevel != null) ? educationalLevel : "No especificado"; }
    public void setEducationalLevel(String educationalLevel) { this.educationalLevel = educationalLevel; }

    public String getDescription() { return (description != null) ? description : "Sin descripción"; }
    public void setDescription(String description) { this.description = description; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return (teacherName != null) ? teacherName : "No asignado"; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public List<StudentResponse> getStudents() { return students; }
    public void setStudents(List<StudentResponse> students) { this.students = students; }

    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }

    public Integer getExamCount() { return examCount; }
    public void setExamCount(Integer examCount) { this.examCount = examCount; }
}