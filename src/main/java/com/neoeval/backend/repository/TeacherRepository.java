package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);

    // ✅ Nuevo: Encontrar profesores por asignatura
    @Query("SELECT t FROM Teacher t JOIN t.subjects s WHERE s.id = :subjectId")
    List<Teacher> findTeachersBySubjectId(@Param("subjectId") Long subjectId);

    // ✅ Nuevo: Encontrar profesores sin asignaturas
    @Query("SELECT t FROM Teacher t WHERE t.subjects IS EMPTY")
    List<Teacher> findTeachersWithoutSubjects();
}