package com.neoeval.backend.repository;

import com.neoeval.backend.entity.StudentResult;
import com.neoeval.backend.dto.response.ExamSummaryResponse; // 👈 NUEVA IMPORTACIÓN
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // 👈 NUEVA IMPORTACIÓN para @Param
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    // Método útil para calcular las métricas de progreso (ej. Promedio)
    List<StudentResult> findByStudentId(Long studentId);

    // Obtiene solo los IDs de los exámenes que el estudiante ya completó.
    @Query("SELECT sr.exam.id FROM StudentResult sr WHERE sr.student.id = :studentId")
    List<Long> findCompletedExamIdsByStudentId(Long studentId);

    /**
     * Consulta optimizada (JOIN FETCH) para cargar Exam y Subject junto con el resultado.
     * Ordena por fecha de finalización descendente (el más reciente primero).
     */
    @Query("SELECT sr FROM StudentResult sr JOIN FETCH sr.exam e LEFT JOIN FETCH e.subject s WHERE sr.student.id = :studentId ORDER BY sr.completedAt DESC")
    List<StudentResult> findResultsWithExamAndSubjectByStudentId(Long studentId);

    /**
     * 🚀 NUEVO: Consulta para calcular el promedio de puntuación de TODOS los estudiantes.
     * Devuelve una lista de arrays: [ID_Estudiante (Long), Promedio_Score (Double)]
     */
    @Query("SELECT sr.student.id, AVG(sr.percentage) FROM StudentResult sr GROUP BY sr.student.id")
    List<Object[]> findAllStudentAverages();

    // =====================================================================
    // ✅ NUEVO MÉTODO PARA EL RESUMEN DEL PROFESOR
    // =====================================================================

    /**
     * Calcula el resumen de resultados (promedio de Score, conteo, última fecha)
     * para todos los exámenes creados por un profesor específico.
     * Usa el constructor del DTO ExamSummaryResponse.
     */
    @Query("SELECT new com.neoeval.backend.dto.response.ExamSummaryResponse(" +
            "e.id, e.title, AVG(r.score), COUNT(r.id), MAX(r.completedAt), e.subject.name) " + // 👈 ¡Nombre de la materia añadido!
            "FROM StudentResult r JOIN r.exam e JOIN e.teacher t " +
            "WHERE t.id = :teacherId " +
            "GROUP BY e.id, e.title, e.subject.name " + // 👈 ¡Agrupación por nombre de la materia añadida!
            "ORDER BY MAX(r.completedAt) DESC")
    List<ExamSummaryResponse> findExamSummariesByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 🚀 NUEVO: Obtiene todos los resultados de estudiantes para un examen específico.
     * Incluye JOIN FETCH para Student para cargar el nombre y el correo electrónico.
     */
    @Query("SELECT sr FROM StudentResult sr JOIN FETCH sr.student s WHERE sr.exam.id = :examId ORDER BY sr.percentage DESC")
    List<StudentResult> findResultsWithStudentByExamId(@Param("examId") Long examId);
}