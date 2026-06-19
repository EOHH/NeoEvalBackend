package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // ✅ Métodos existentes...
    Optional<Subject> findByName(String name);
    boolean existsByName(String name);

    // ✅ Nuevo: Verificar por código
    Optional<Subject> findByCode(String code);
    boolean existsByCode(String code);

    @Query(value = "SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId",
           countQuery = "SELECT count(s) FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
    Page<Subject> findSubjectsByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query(value = "SELECT s FROM Subject s JOIN s.classGroups cg WHERE cg.id = :groupId",
           countQuery = "SELECT count(s) FROM Subject s JOIN s.classGroups cg WHERE cg.id = :groupId")
    Page<Subject> findSubjectsByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    Page<Subject> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Subject> findByEducationalLevel(String educationalLevel, Pageable pageable);
    Page<Subject> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    // ✅ Nuevos métodos para búsquedas avanzadas
    Page<Subject> findByCredits(Integer credits, Pageable pageable);
    Page<Subject> findByHoursPerWeek(Integer hoursPerWeek, Pageable pageable);
    Page<Subject> findBySemester(Subject.Semester semester, Pageable pageable);
    Page<Subject> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Subject> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    @Query(value = "SELECT s FROM Subject s WHERE s.credits BETWEEN :minCredits AND :maxCredits",
           countQuery = "SELECT count(s) FROM Subject s WHERE s.credits BETWEEN :minCredits AND :maxCredits")
    Page<Subject> findByCreditsRange(@Param("minCredits") Integer minCredits,
                                     @Param("maxCredits") Integer maxCredits, Pageable pageable);

    @Query("SELECT s FROM Subject s WHERE s.credits = :credits AND s.semester = :semester")
    List<Subject> findByCreditsAndSemester(@Param("credits") Integer credits,
                                           @Param("semester") Subject.Semester semester);

    @Query("SELECT s.educationalLevel, COUNT(s) FROM Subject s GROUP BY s.educationalLevel")
    List<Object[]> countSubjectsByEducationalLevel();

    @Query("SELECT s FROM Subject s WHERE s.teachers IS EMPTY")
    List<Subject> findSubjectsWithoutTeachers();

    @Query("SELECT s FROM Subject s WHERE s.classGroups IS EMPTY")
    List<Subject> findSubjectsWithoutGroups();

    // ✅ Nuevo: Contar por estado activo
    @Query("SELECT s.isActive, COUNT(s) FROM Subject s GROUP BY s.isActive")
    List<Object[]> countSubjectsByActiveStatus();
}