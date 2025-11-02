package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.StudentAchievementRequest;
import com.neoeval.backend.dto.response.StudentAchievementResponse; // 👈 ¡NUEVA IMPORTACIÓN CLAVE!
import com.neoeval.backend.entity.Achievement;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.entity.StudentAchievement;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.exception.ValidationException;
import com.neoeval.backend.repository.AchievementRepository;
import com.neoeval.backend.repository.StudentAchievementRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.service.StudentAchievementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentAchievementServiceImpl implements StudentAchievementService {

    private final StudentAchievementRepository studentAchievementRepository;
    private final AchievementRepository achievementRepository;
    private final StudentRepository studentRepository;

    public StudentAchievementServiceImpl(
            StudentAchievementRepository studentAchievementRepository,
            AchievementRepository achievementRepository,
            StudentRepository studentRepository) {
        this.studentAchievementRepository = studentAchievementRepository;
        this.achievementRepository = achievementRepository;
        this.studentRepository = studentRepository;
    }

    // ----------------------------------------------------------------------
    // 🥇 MÉTODO CORREGIDO: awardAchievement
    // ----------------------------------------------------------------------
    @Override
    @Transactional
    public StudentAchievementResponse awardAchievement(StudentAchievementRequest request) { // 👈 Firma actualizada
        // 1. Verificar existencia de entidades
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", request.getStudentId()));

        Achievement achievement = achievementRepository.findById(request.getAchievementId())
                .orElseThrow(() -> new ResourceNotFoundException("Logro", "id", request.getAchievementId()));

        // 2. Verificar si el logro ya fue otorgado
        if (studentAchievementRepository.existsByStudentAndAchievement(student, achievement)) {
            throw new ValidationException("El estudiante ya tiene el logro '" + achievement.getName() + "'.");
        }

        // 3. Crear y guardar la entidad de unión
        StudentAchievement sa = new StudentAchievement(student, achievement);
        StudentAchievement savedSa = studentAchievementRepository.save(sa);

        // 4. Devolver el DTO de respuesta ligero
        return mapToStudentAchievementResponse(savedSa); // 👈 ¡Ahora devuelve DTO!
    }

    @Override
    @Transactional
    public void assignAchievements(Student student, Integer totalPoints) {
        // Lógica de asignación automática (se mantiene igual)
        List<Achievement> eligibleAchievements = achievementRepository
                .findByRequiredPointsLessThanEqual(totalPoints);

        Set<Long> alreadyAchievedIds = studentAchievementRepository
                .findByStudent(student).stream()
                .map(sa -> sa.getAchievement().getId())
                .collect(Collectors.toSet());

        eligibleAchievements.stream()
                .filter(achievement -> !alreadyAchievedIds.contains(achievement.getId()))
                .forEach(newAchievement -> {
                    StudentAchievement sa = new StudentAchievement(student, newAchievement);
                    studentAchievementRepository.save(sa);
                });
    }

    @Override
    public long countStudentsAchieved(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Logro", "id", achievementId));

        return studentAchievementRepository.countByAchievement(achievement);
    }

    // ----------------------------------------------------------------------
    // 💡 MÉTODO DE CONSULTA CORREGIDO: getAchievementsByStudent
    // ----------------------------------------------------------------------
    @Override
    public List<StudentAchievementResponse> getAchievementsByStudent(Long studentId) { // 👈 Firma actualizada
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        // Busca las entidades y las mapea inmediatamente a DTOs
        return studentAchievementRepository.findByStudent(student).stream()
                .map(this::mapToStudentAchievementResponse) // 👈 Mapeo a DTOs
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------------
    // 🧩 MÉTODO AUXILIAR DE MAPEO
    // ----------------------------------------------------------------------
    private StudentAchievementResponse mapToStudentAchievementResponse(StudentAchievement sa) {
        StudentAchievementResponse response = new StudentAchievementResponse();
        response.setId(sa.getId());
        response.setAchievementDate(sa.getAchievementDate());

        // Mapea SOLO los IDs y nombres
        if (sa.getStudent() != null) {
            response.setStudentId(sa.getStudent().getId());
            response.setStudentName(sa.getStudent().getName());
        }
        if (sa.getAchievement() != null) {
            response.setAchievementId(sa.getAchievement().getId());
            response.setAchievementName(sa.getAchievement().getName());
        }

        return response;
    }
}