package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.QuizSubmissionRequest;
import com.neoeval.backend.dto.response.StudentResultResponse;
import com.neoeval.backend.entity.StudentResult;
import com.neoeval.backend.dto.response.StudentExamResultDetailResponse;
import com.neoeval.backend.dto.request.UpdateResultScoreRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface StudentResultService {

    StudentResult processQuizSubmission(QuizSubmissionRequest request);

    // ✅ La interfaz ya está correcta para devolver la lista de DTOs
    Page<StudentResultResponse> getResultsByStudent(Long studentId, Pageable pageable);

    Page<StudentExamResultDetailResponse> getStudentResultsByExam(Long examId, Pageable pageable);

    StudentExamResultDetailResponse updateResultScore(Long resultId, UpdateResultScoreRequest request);
}