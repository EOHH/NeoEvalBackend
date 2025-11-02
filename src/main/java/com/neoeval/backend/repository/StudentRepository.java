package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("SELECT s FROM Student s JOIN s.classGroups cg WHERE cg.teacher.id = :teacherId")
    List<Student> findStudentsByGroupTeacherId(@Param("teacherId") Long teacherId);

    List<Student> findByNameContainingIgnoreCase(String name);
}