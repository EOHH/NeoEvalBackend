package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class AssignExamRequest {
    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Student IDs are required")
    @Size(min = 1, message = "At least one student ID must be provided")
    private List<Long> studentIds;

    // Getters and Setters
    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }
}