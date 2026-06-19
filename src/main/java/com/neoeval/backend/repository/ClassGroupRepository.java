package com.neoeval.backend.repository;

import com.neoeval.backend.entity.ClassGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {

    Optional<ClassGroup> findByName(String name);
    boolean existsByName(String name);

    // ✅ Encontrar grupos por asignatura
    @Query(value = "SELECT cg FROM ClassGroup cg JOIN cg.subjects s WHERE s.id = :subjectId",
           countQuery = "SELECT count(cg) FROM ClassGroup cg JOIN cg.subjects s WHERE s.id = :subjectId")
    Page<ClassGroup> findClassGroupsBySubjectId(@Param("subjectId") Long subjectId, Pageable pageable);

    // ✅ Encontrar grupos sin asignaturas
    @Query(value = "SELECT cg FROM ClassGroup cg WHERE cg.subjects IS EMPTY",
           countQuery = "SELECT count(cg) FROM ClassGroup cg WHERE cg.subjects IS EMPTY")
    Page<ClassGroup> findClassGroupsWithoutSubjects(Pageable pageable);

    // Correcto: Para encontrar ClassGroups por el ID del Teacher asociado
    // Spring Data JPA lo traduce a: SELECT cg FROM ClassGroup cg WHERE cg.teacher.id = :teacherId
    Page<ClassGroup> findByTeacher_Id(Long teacherId, Pageable pageable);

    // Correcto: Para contar ClassGroups por el ID del Teacher asociado
    // Spring Data JPA lo traduce a: SELECT COUNT(cg) FROM ClassGroup cg WHERE cg.teacher.id = :teacherId
    Long countByTeacher_Id(Long teacherId);

    // Si tu ClassGroup realmente tiene un campo 'createdBy' que es una entidad User,
    // y quieres contar/encontrar por el ID de ese User (diferente al Teacher),
    // ENTONCES necesitarías añadir el campo 'createdBy' a ClassGroup, como se discutió anteriormente.
    // Si no tienes ese campo, los siguientes métodos CAUSARÁN UN ERROR:
    // List<ClassGroup> findByCreatedById(Long createdById); // ESTO CAUSARÁ UN ERROR si no hay 'createdBy' en ClassGroup
    // Long countByCreatedById(Long createdById);             // ESTO CAUSARÁ UN ERROR si no hay 'createdBy' en ClassGroup
}