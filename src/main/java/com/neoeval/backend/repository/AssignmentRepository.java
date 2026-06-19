package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository // Asegúrate de que está anotado con @Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // findById y save ya están provistos por JpaRepository.
    Page<Assignment> findByStudentId(Long studentId, Pageable pageable); // Si necesitas obtener asignaciones por estudiante
    Page<Assignment> findByExamId(Long examId, Pageable pageable); // Si necesitas obtener asignaciones por examen
}