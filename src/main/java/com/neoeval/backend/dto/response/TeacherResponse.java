package com.neoeval.backend.dto.response;

import com.neoeval.backend.entity.User;

public class TeacherResponse extends UserResponse {
    private Integer groupsCreated;
    private Integer examsCreated;
    private Integer studentsTaught;

    // ✅ Constructor que recibe User (llama al constructor del padre)
    public TeacherResponse(User user) {
        super(user);
    }

    // Getters y Setters
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
