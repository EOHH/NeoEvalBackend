package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.StudentAchievementRequest;
import com.neoeval.backend.dto.response.StudentAchievementResponse; // 👈 Nueva importación
import com.neoeval.backend.entity.Student;
import java.util.List;

public interface StudentAchievementService {

    // Cambia StudentAchievement a StudentAchievementResponse
    StudentAchievementResponse awardAchievement(StudentAchievementRequest request);

    void assignAchievements(Student student, Integer totalPoints);

    long countStudentsAchieved(Long achievementId);

    // Cambia List<StudentAchievement> a List<StudentAchievementResponse>
    List<StudentAchievementResponse> getAchievementsByStudent(Long studentId);
}