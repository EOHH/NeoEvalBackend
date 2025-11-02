package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.AchievementRequest;
import com.neoeval.backend.dto.response.AchievementResponse;
import com.neoeval.backend.entity.Student; // 👈 Importación de la entidad Student
import java.util.List;

public interface AchievementService {
    AchievementResponse createAchievement(AchievementRequest achievementRequest);
    AchievementResponse getAchievementById(Long id);
    List<AchievementResponse> getAllAchievements();
    AchievementResponse updateAchievement(Long id, AchievementRequest achievementRequest);
    void deleteAchievement(Long id);
    AchievementResponse getAchievementByName(String name);
    List<AchievementResponse> getAchievementsByPointsLessOrEqualTo(Integer points);

    // ✅ NUEVA FUNCIONALIDAD: Asignación automática de logros
    void assignAchievements(Student student, Integer totalPoints);
}