package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Achievement; // Importación necesaria para el nuevo método
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.entity.StudentAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAchievementRepository extends JpaRepository<StudentAchievement, Long> {

    // 1. Ya lo tienes: Encuentra todos los logros de un estudiante.
    List<StudentAchievement> findByStudent(Student student);

    // 2. ¡NUEVO! Útil para la lógica de negocio (p. ej., "No se puede otorgar dos veces").
    // Verifica si ya existe una combinación específica de Estudiante y Logro.
    boolean existsByStudentAndAchievement(Student student, Achievement achievement);

    // 3. ¡NUEVO! Útil para tu AchievementResponse (studentsAchieved).
    // Cuenta cuántos registros de StudentAchievement existen para un logro específico.
    long countByAchievement(Achievement achievement);
}