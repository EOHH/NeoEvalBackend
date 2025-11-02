package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.AchievementRequest;
import com.neoeval.backend.dto.response.AchievementResponse;
import com.neoeval.backend.entity.Achievement;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.AchievementRepository;
import com.neoeval.backend.service.AchievementService;
import com.neoeval.backend.service.StudentAchievementService; // Importación clave
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final StudentAchievementService studentAchievementService;

    // Constructor con Inyección de Dependencias
    public AchievementServiceImpl(
            AchievementRepository achievementRepository,
            StudentAchievementService studentAchievementService) {
        this.achievementRepository = achievementRepository;
        this.studentAchievementService = studentAchievementService;
    }

    // --- 1. MÉTODOS CRUD (Create, Update, Delete) ---

    @Override
    @Transactional
    public AchievementResponse createAchievement(AchievementRequest achievementRequest) {
        // Mapeo de Request DTO a Entidad
        Achievement achievement = new Achievement();
        achievement.setName(achievementRequest.getName());
        achievement.setDescription(achievementRequest.getDescription());
        achievement.setImageUrl(achievementRequest.getImageUrl());
        achievement.setRequiredPoints(achievementRequest.getRequiredPoints());

        // Guardar y Mapear a Response DTO
        Achievement savedAchievement = achievementRepository.save(achievement);
        return mapToAchievementResponse(savedAchievement);
    }

    @Override
    @Transactional
    public AchievementResponse updateAchievement(Long id, AchievementRequest achievementRequest) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logro", "id", id));

        // Actualizar campos
        achievement.setName(achievementRequest.getName());
        achievement.setDescription(achievementRequest.getDescription());
        achievement.setImageUrl(achievementRequest.getImageUrl());
        achievement.setRequiredPoints(achievementRequest.getRequiredPoints());

        Achievement updatedAchievement = achievementRepository.save(achievement);
        return mapToAchievementResponse(updatedAchievement);
    }

    @Override
    @Transactional
    public void deleteAchievement(Long id) {
        if (!achievementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Logro", "id", id);
        }
        // La eliminación de StudentAchievement se maneja por la cascada en la entidad Achievement
        achievementRepository.deleteById(id);
    }

    // --- 2. MÉTODOS DE LECTURA (Read) ---

    @Override
    public AchievementResponse getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Logro", "id", id));
        return mapToAchievementResponse(achievement);
    }

    @Override
    public List<AchievementResponse> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(this::mapToAchievementResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AchievementResponse getAchievementByName(String name) {
        Achievement achievement = achievementRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Logro", "nombre", name));
        return mapToAchievementResponse(achievement);
    }

    @Override
    public List<AchievementResponse> getAchievementsByPointsLessOrEqualTo(Integer points) {
        List<Achievement> achievements = achievementRepository.findByRequiredPointsLessThanEqual(points);
        return achievements.stream()
                .map(this::mapToAchievementResponse)
                .collect(Collectors.toList());
    }

    // --- 3. LÓGICA DE NEGOCIO (Delegada) ---

    @Override
    @Transactional
    public void assignAchievements(Student student, Integer totalPoints) {
        // 🚀 Delegación de la lógica de asignación al StudentAchievementService
        studentAchievementService.assignAchievements(student, totalPoints);
    }


    // --- 4. MÉTODO DE MAPEO ---

    /**
     * Convierte una entidad Achievement a su DTO de respuesta.
     * Incluye el conteo de estudiantes (studentsAchieved) obtenido del StudentAchievementService.
     */
    private AchievementResponse mapToAchievementResponse(Achievement achievement) {
        AchievementResponse response = new AchievementResponse();
        response.setId(achievement.getId());
        response.setName(achievement.getName());
        response.setDescription(achievement.getDescription());
        response.setImageUrl(achievement.getImageUrl());
        response.setRequiredPoints(achievement.getRequiredPoints());

        // ✅ Uso del servicio delegado para obtener el conteo de estudiantes
        long studentsAchievedCount = studentAchievementService.countStudentsAchieved(achievement.getId());
        response.setStudentsAchieved((int) studentsAchievedCount);

        return response;
    }
}