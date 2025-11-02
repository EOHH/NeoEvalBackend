package com.neoeval.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ExamResponse {
    private Long id;
    private String title;
    private String description;
    private String examType;

    private LocalDateTime openingDate;
    private LocalDateTime closingDate;

    private Integer timeLimitMinutes;
    private Integer allowedAttempts;
    private Double averageDifficulty;

    private Long subjectId;
    private String subjectName;

    private ClassGroupResponse classGroup;

    private Long teacherId;
    private String teacherName;

    private Boolean isCompleted;

    private List<QuestionResponse> questions;
    private Integer questionCount;

    // Constructor vacío
    public ExamResponse() {}

    // Constructor con todos los campos
    public ExamResponse(Long id, String title, String description, String examType, LocalDateTime openingDate,
                        LocalDateTime closingDate, Integer timeLimitMinutes, Integer allowedAttempts,
                        Double averageDifficulty, Long subjectId, String subjectName, ClassGroupResponse classGroup,
                        Long teacherId, String teacherName, Boolean isCompleted,
                        List<QuestionResponse> questions, Integer questionCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.examType = examType;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.timeLimitMinutes = timeLimitMinutes;
        this.allowedAttempts = allowedAttempts;
        this.averageDifficulty = averageDifficulty;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.classGroup = classGroup;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.isCompleted = isCompleted;
        this.questions = questions;
        this.questionCount = questionCount;
    }

    // Getters y Setters completos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public LocalDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Integer getAllowedAttempts() {
        return allowedAttempts;
    }

    public void setAllowedAttempts(Integer allowedAttempts) {
        this.allowedAttempts = allowedAttempts;
    }

    public Double getAverageDifficulty() {
        return averageDifficulty;
    }

    public void setAverageDifficulty(Double averageDifficulty) {
        this.averageDifficulty = averageDifficulty;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ClassGroupResponse getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroupResponse classGroup) {
        this.classGroup = classGroup;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
}
