package com.neoeval.backend.repository;

import com.neoeval.backend.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {

    // Buscar todas las sesiones que pertenecen a un módulo, ordenadas por el índice
    List<ClassSession> findByCourseModuleIdOrderByOrderIndexAsc(Long courseModuleId);

    // Buscar una sesión específica dentro de un módulo
    Optional<ClassSession> findByIdAndCourseModuleId(Long id, Long courseModuleId);

    // Buscar el ordenIndex máximo de las sesiones de un módulo (útil para asignar el siguiente índice)
    @Query("SELECT MAX(s.orderIndex) FROM ClassSession s WHERE s.courseModule.id = :courseModuleId")
    Optional<Integer> findMaxOrderIndexByCourseModuleId(@Param("courseModuleId") Long courseModuleId);
}