package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Por si necesitas encontrar por nombre u otros atributos

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // Puedes añadir métodos de consulta personalizados si los necesitas, por ejemplo:
    Optional<Achievement> findByName(String name);
    List<Achievement> findByRequiredPointsLessThanEqual(Integer points);
}