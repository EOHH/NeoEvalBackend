package com.neoeval.backend.repository;

import com.neoeval.backend.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    // Buscar todos los módulos creados por un profesor específico
    Page<CourseModule> findByTeacherId(Long teacherId, Pageable pageable);

    // Buscar todos los módulos asociados a una asignatura específica
    Page<CourseModule> findBySubjectId(Long subjectId, Pageable pageable);

    // Contar el número de módulos por profesor (útil para DTOs de Profesor)
    Long countByTeacherId(Long teacherId);

    // Buscar un módulo por ID y asegurarse de que pertenezca a un profesor
    CourseModule findByIdAndTeacherId(Long id, Long teacherId);

    /**
     * ✅ NUEVO: Recupera todos los CourseModule para un estudiante dado su ID.
     * La lógica es:
     * 1. JOIN de CourseModule (cm) a Subject (s).
     * 2. JOIN de Subject (s) a ClassGroup (cg).
     * 3. JOIN de ClassGroup (cg) a Student (st).
     * 4. Filtra donde el ID del Student (st.id) coincida.
     * DISTINCT asegura que los módulos no se dupliquen si un estudiante está en varios grupos con la misma asignatura.
     */
    @Query(value = "SELECT DISTINCT cm FROM CourseModule cm " +
            "JOIN cm.classGroup cg " +     // Enlaza el Módulo con su Grupo asignado
            "JOIN cg.students st " +       // Enlaza el Grupo con sus Estudiantes
            "WHERE st.id = :studentId " +  // Usa el parámetro de la firma del método
            "ORDER BY cm.createdAt DESC",
           countQuery = "SELECT count(DISTINCT cm) FROM CourseModule cm JOIN cm.classGroup cg JOIN cg.students st WHERE st.id = :studentId")
    Page<CourseModule> findModulesByStudentId(@Param("studentId") Long studentId, Pageable pageable);
}