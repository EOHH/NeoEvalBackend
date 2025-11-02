package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph; // ⬅️ ¡Nueva Importación!
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional; // ⬅️ ¡Necesaria para Optional!

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    // 🟢 MÉTODO CLAVE: Usa EntityGraph para cargar Questions y sus Answers en una sola consulta.
    @EntityGraph(attributePaths = {"questions", "questions.answers"})
    Optional<Exam> findExamById(Long id);

    List<Exam> findByTeacher_Id(Long teacherId);
    List<Exam> findByClassGroup_Id(Long groupId);
    Long countByTeacher_Id(Long teacherId);

    // 🚀 NUEVA CONSULTA: Solo filtra por ID de grupo
    @Query("SELECT e FROM Exam e " +
            "WHERE e.classGroup.id IN (:classGroupIds)")
    List<Exam> findAllAssignedExamsForStudent(
            @Param("classGroupIds") List<Long> classGroupIds
    );

    @Deprecated
    @Query("SELECT e FROM Exam e " +
            "WHERE e.classGroup.id IN (:classGroupIds) " +
            "AND e.openingDate <= :now " +
            "AND (e.closingDate IS NULL OR e.closingDate >= :now) " +
            "AND e.id NOT IN (:completedExamIds)")
    List<Exam> findAvailableExamsForStudent(
            @Param("classGroupIds") List<Long> classGroupIds,
            @Param("now") Instant now,
            @Param("completedExamIds") List<Long> completedExamIds
    );
}