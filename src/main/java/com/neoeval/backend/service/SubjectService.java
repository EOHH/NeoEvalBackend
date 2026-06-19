package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.CreateSubjectRequest;
import com.neoeval.backend.dto.response.SubjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubjectService {
    Page<SubjectResponse> getAllSubjects(Pageable pageable);
    SubjectResponse getSubjectById(Long id);
    SubjectResponse createSubject(CreateSubjectRequest request);
    SubjectResponse updateSubject(Long id, CreateSubjectRequest request);
    void deleteSubject(Long id);
    Page<SubjectResponse> getSubjectsByTeacher(Long teacherId, Pageable pageable);
    Page<SubjectResponse> getSubjectsByStudent(Long studentId, Pageable pageable);
    Page<SubjectResponse> searchSubjects(String query, Pageable pageable);
    SubjectResponse assignTeacherToSubject(Long subjectId, Long teacherId);
    void removeTeacherFromSubject(Long subjectId, Long teacherId);
}