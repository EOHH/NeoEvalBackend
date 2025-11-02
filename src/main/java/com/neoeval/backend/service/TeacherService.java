package com.neoeval.backend.service;

import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.TeacherResponse;
import com.neoeval.backend.dto.response.ExamResponse;   // Asegúrate de importar ExamResponse

import java.util.List;

public interface TeacherService {
    TeacherResponse getTeacherById(Long id);
    List<TeacherResponse> getAllTeachers();
    List<ClassGroupResponse> getGroupsByTeacher(Long id);
    List<ExamResponse> getExamsByTeacher(Long id);
    // Si TeacherController tuviera métodos para crear/actualizar profesores, también irían aquí.
}