package com.neoeval.backend.repository;

import com.neoeval.backend.entity.PracticeExercise;
import com.neoeval.backend.entity.enums.DifficultyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeExerciseRepository extends JpaRepository<PracticeExercise, Long> {

    @EntityGraph(attributePaths = {"category"})
    Page<PracticeExercise> findByCategory_IdAndDifficultyLevel(Long categoryId, DifficultyLevel level, Pageable pageable);
}
