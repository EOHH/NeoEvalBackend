package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.User;

public class ParentResponse extends UserResponse {
    private String relationship;
    private Long studentId;
    private String studentName;
    private String studentEducationalLevel;

    // ✅ Constructor que recibe User (llama al constructor del padre)
    public ParentResponse(User user) {
        super(user);
    }

    // Getters y Setters
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
