package com.neoeval.backend.dto.response;

import java.time.Instant; // Importamos Instant para el constructor de la clase padre

public class TeacherResponse extends UserResponse {
    private Integer groupsCreated;
    private Integer examsCreated;
    private Integer studentsTaught;

    // Constructor vacío
    public TeacherResponse() {
        super();
    }

    // Constructor completo para facilitar el mapeo
    public TeacherResponse(Long id, String name, String email, String userType, Instant createdAt, Instant lastLogin, boolean active, Integer groupsCreated, Integer examsCreated, Integer studentsTaught) {
        // ✅ Llama al constructor de la clase padre (UserResponse) usando Instant
        super(id, name, email, userType, createdAt, lastLogin, active);
        this.groupsCreated = groupsCreated;
        this.examsCreated = examsCreated;
        this.studentsTaught = studentsTaught;
    }

    // Getters y Setters para los campos específicos de Teacher
    public Integer getGroupsCreated() {
        return groupsCreated;
    }

    public void setGroupsCreated(Integer groupsCreated) {
        this.groupsCreated = groupsCreated;
    }

    public Integer getExamsCreated() {
        return examsCreated;
    }

    public void setExamsCreated(Integer examsCreated) {
        this.examsCreated = examsCreated;
    }

    public Integer getStudentsTaught() {
        return studentsTaught;
    }

    public void setStudentsTaught(Integer studentsTaught) {
        this.studentsTaught = studentsTaught;
    }
}