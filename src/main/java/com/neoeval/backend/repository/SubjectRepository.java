package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
    List<Subject> findSubjectsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT s FROM Subject s JOIN s.classGroups cg WHERE cg.id = :groupId")
    List<Subject> findSubjectsByGroupId(@Param("groupId") Long groupId);

    List<Subject> findByNameContainingIgnoreCase(String name);
    List<Subject> findByEducationalLevel(String educationalLevel);
    List<Subject> findByDescriptionContainingIgnoreCase(String description);

    // ✅ Nuevos métodos para búsquedas avanzadas
    List<Subject> findByCredits(Integer credits);
    List<Subject> findByHoursPerWeek(Integer hoursPerWeek);
    List<Subject> findBySemester(Subject.Semester semester);
    List<Subject> findByIsActive(Boolean isActive);

    List<Subject> findByCodeContainingIgnoreCase(String code);

    @Query("SELECT s FROM Subject s WHERE s.credits BETWEEN :minCredits AND :maxCredits")
    List<Subject> findByCreditsRange(@Param("minCredits") Integer minCredits,
                                     @Param("maxCredits") Integer maxCredits);

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