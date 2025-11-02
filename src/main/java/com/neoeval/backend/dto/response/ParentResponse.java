package com.neoeval.backend.dto.response;

import java.time.Instant; // Importamos Instant para el constructor de la clase padre

public class ParentResponse extends UserResponse {
    private String relationship;
    private Long studentId;
    private String studentName;
    private String studentEducationalLevel;

    // Constructor vacío
    public ParentResponse() {
        super();
    }

    // Constructor completo para facilitar el mapeo
    public ParentResponse(Long id, String name, String email, String userType, Instant createdAt, Instant lastLogin, boolean active, String relationship, Long studentId, String studentName, String studentEducationalLevel) {
        // ✅ Llama al constructor de la clase padre (UserResponse) usando Instant
        super(id, name, email, userType, createdAt, lastLogin, active);
        this.relationship = relationship;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEducationalLevel = studentEducationalLevel;
    }

    // Getters y Setters para los campos específicos de Parent
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEducationalLevel() {
        return studentEducationalLevel;
    }

    public void setStudentEducationalLevel(String studentEducationalLevel) {
        this.studentEducationalLevel = studentEducationalLevel;
    }
}