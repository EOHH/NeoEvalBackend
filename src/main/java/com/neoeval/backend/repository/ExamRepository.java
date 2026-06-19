package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph; // ⬅️ ¡Nueva Importación!
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Optional; // ⬅️ ¡Necesaria para Optional!

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    // 🟢 MÉTODO CLAVE: Usa EntityGraph para cargar Questions y sus Answers en una sola consulta.
    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    Optional<Exam> findExamById(Long id);

    Page<Exam> findByTeacher_Id(Long teacherId, Pageable pageable);
    Page<Exam> findByClassGroup_Id(Long groupId, Pageable pageable);
    Long countByTeacher_Id(Long teacherId);

    // 🚀 NUEVA CONSULTA: Solo filtra por ID de grupo
    @Query(value = "SELECT e FROM Exam e " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.classGroup " +
            "WHERE e.classGroup.id IN (:classGroupIds)",
            countQuery = "SELECT count(e) FROM Exam e WHERE e.classGroup.id IN (:classGroupIds)")
    Page<Exam> findAllAssignedExamsForStudent(
            @Param("classGroupIds") java.util.List<Long> classGroupIds,
            Pageable pageable
    );

    @Deprecated
    @Query(value = "SELECT e FROM Exam e " +
            "LEFT JOIN FETCH e.teacher " +
            "LEFT JOIN FETCH e.classGroup " +
            "WHERE e.classGroup.id IN (:classGroupIds) " +
            "AND e.openingDate <= :now " +
            "AND (e.closingDate IS NULL OR e.closingDate >= :now) " +
            "AND e.id NOT IN (:completedExamIds)",
            countQuery = "SELECT count(e) FROM Exam e WHERE e.classGroup.id IN (:classGroupIds) AND e.openingDate <= :now AND (e.closingDate IS NULL OR e.closingDate >= :now) AND e.id NOT IN (:completedExamIds)")
    Page<Exam> findAvailableExamsForStudent(
            @Param("classGroupIds") java.util.List<Long> classGroupIds,
            @Param("now") Instant now,
            @Param("completedExamIds") java.util.List<Long> completedExamIds,
            Pageable pageable
    );
}