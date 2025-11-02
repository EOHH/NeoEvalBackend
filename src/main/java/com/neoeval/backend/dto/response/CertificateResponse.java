package com.neoeval.backend.dto.response;

import java.time.Instant; // ✅ Importado Instant

public class CertificateResponse {
    private Long id;
    private String title;
    private String description;
    private Instant issueDate; // ✅ Actualizado a Instant
    private String fileUrl;
    private String verificationCode;
    private Long studentId;
    private String studentName;
    private Long examId;
    private String examTitle;

    // Constructor vacío
    public CertificateResponse() {
    }

    // Constructor con todos los campos
    public CertificateResponse(Long id, String title, String description, Instant issueDate, String fileUrl,
                               String verificationCode, Long studentId, String studentName, Long examId, String examTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.issueDate = issueDate;
        this.fileUrl = fileUrl;
        this.verificationCode = verificationCode;
        this.studentId = studentId;
        this.studentName = studentName;
        this.examId = examId;
        this.examTitle = examTitle;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Instant getIssueDate() { return issueDate; }
    public void setIssueDate(Instant issueDate) { this.issueDate = issueDate; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
}