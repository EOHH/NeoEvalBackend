package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Asegúrate de que está anotado con @Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // findById y save ya están provistos por JpaRepository.
    List<Assignment> findByStudentId(Long studentId); // Si necesitas obtener asignaciones por estudiante
    List<Assignment> findByExamId(Long examId); // Si necesitas obtener asignaciones por examen
}