package com.neoeval.backend.dto.response;

import java.time.Instant; // ✅ Importado Instant
import java.util.List;
// Importa AnswerResponse si no está en el mismo paquete
// import com.neoeval.backend.dto.response.AnswerResponse;

public class AssignmentResponse {
    private Long id;
    private Instant assignedDate; // ✅ Actualizado a Instant
    private boolean completed;
    private Instant completionDate; // ✅ Actualizado a Instant
    private Double score;
    private Long studentId;
    private String studentName;
    private Long examId;
    private String examTitle;
    private List<AnswerResponse> answers;

    // Constructor vacío (siempre es buena práctica)
    public AssignmentResponse() {
    }

    // Constructor con todos los campos
    public AssignmentResponse(Long id, Instant assignedDate, boolean completed, Instant completionDate, Double score,
                              Long studentId, String studentName, Long examId, String examTitle,
                              List<AnswerResponse> answers) {
        this.id = id;
        this.assignedDate = assignedDate;
        this.completed = completed;
        this.completionDate = completionDate;
        this.score = score;
        this.studentId = studentId;
        this.studentName = studentName;
        this.examId = examId;
        this.examTitle = examTitle;
        this.answers = answers;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getAssignedDate() { return assignedDate; }
    public void setAssignedDate(Instant assignedDate) { this.assignedDate = assignedDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Instant getCompletionDate() { return completionDate; }
    public void setCompletionDate(Instant completionDate) { this.completionDate = completionDate; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public List<AnswerResponse> getAnswers() { return answers; }
    public void setAnswers(List<AnswerResponse> answers) { this.answers = answers; }
}