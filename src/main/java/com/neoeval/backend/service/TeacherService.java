package com.neoeval.backend.service;

import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.TeacherResponse;
import com.neoeval.backend.dto.response.ExamResponse;   // Asegúrate de importar ExamResponse

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TeacherService {
    TeacherResponse getTeacherById(Long id);
    Page<TeacherResponse> getAllTeachers(Pageable pageable);
    Page<ClassGroupResponse> getGroupsByTeacher(Long id, Pageable pageable);
    Page<ExamResponse> getExamsByTeacher(Long id, Pageable pageable);
    // Si TeacherController tuviera métodos para crear/actualizar profesores, también irían aquí.
}