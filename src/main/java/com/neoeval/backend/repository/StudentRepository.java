package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 🟢 La carga de 'parent' es automática (EAGER) y se ha eliminado de la consulta.
    // Solo se usa JOIN FETCH para las colecciones LAZY restantes.
    @Query("SELECT DISTINCT s FROM Student s " +
            // Parent se carga automáticamente (EAGER)
            "LEFT JOIN FETCH s.classGroups cg " +
            "LEFT JOIN FETCH s.sentMessages sm " +
            "LEFT JOIN FETCH s.receivedMessages rm " +
            "LEFT JOIN FETCH s.assignments a " +
            "LEFT JOIN FETCH s.certificates c " +
            "LEFT JOIN FETCH s.achievements ach " +
            "WHERE s.id = :id")
    Optional<Student> findStudentById(@Param("id") Long id);

    // El resto de los métodos se mantienen igual...
    @Query("SELECT COUNT(s) FROM Student s JOIN s.classGroups cg WHERE cg.teacher.id = :teacherId")
    Long countStudentsByGroupTeacherId(@Param("teacherId") Long teacherId);

    @Query(value = "SELECT s FROM Student s JOIN s.classGroups cg WHERE cg.teacher.id = :teacherId",
           countQuery = "SELECT count(s) FROM Student s JOIN s.classGroups cg WHERE cg.teacher.id = :teacherId")
    Page<Student> findStudentsByGroupTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    Page<Student> findByClassGroups_Id(Long groupId, Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
}