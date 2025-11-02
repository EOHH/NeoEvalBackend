package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.CreateSubjectRequest;
import com.neoeval.backend.dto.response.SubjectResponse;
import java.util.List;

public interface SubjectService {
    List<SubjectResponse> getAllSubjects();
    SubjectResponse getSubjectById(Long id);
    SubjectResponse createSubject(CreateSubjectRequest request);
    SubjectResponse updateSubject(Long id, CreateSubjectRequest request);
    void deleteSubject(Long id);
    List<SubjectResponse> getSubjectsByTeacher(Long teacherId);
    List<SubjectResponse> getSubjectsByStudent(Long studentId);
    List<SubjectResponse> searchSubjects(String query);
    SubjectResponse assignTeacherToSubject(Long subjectId, Long teacherId);
    void removeTeacherFromSubject(Long subjectId, Long teacherId);
}