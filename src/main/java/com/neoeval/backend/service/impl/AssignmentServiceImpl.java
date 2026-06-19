package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.AssignExamRequest;
import com.neoeval.backend.dto.request.SubmitAnswerRequest;
import com.neoeval.backend.dto.response.AssignmentResponse;
import com.neoeval.backend.entity.*;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.exception.ValidationException;
import com.neoeval.backend.repository.*;
import com.neoeval.backend.service.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; // ✅ Usar Instant.now()
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public AssignmentServiceImpl(
            AssignmentRepository assignmentRepository,
            StudentRepository studentRepository,
            ExamRepository examRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    @Override
    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + id + " no encontrada."));
        return mapToAssignmentResponse(assignment);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByStudentId(Long studentId, Pageable pageable) {
        return assignmentRepository.findByStudentId(studentId, pageable)
                .map(this::mapToAssignmentResponse);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByExamId(Long examId, Pageable pageable) {
        return assignmentRepository.findByExamId(examId, pageable)
                .map(this::mapToAssignmentResponse);
    }

    @Override
    @Transactional
    public AssignmentResponse submitAssignment(Long assignmentId, List<SubmitAnswerRequest> answers) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + assignmentId + " no encontrada."));

        if (assignment.isCompleted()) {
            throw new IllegalStateException("Esta asignación ya ha sido completada.");
        }

        // ✅ CORRECCIÓN 1: Usar Instant.now() (coincide con la Entidad)
        assignment.setCompleted(true);
        assignment.setCompletionDate(Instant.now());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return mapToAssignmentResponse(updatedAssignment);
    }

    @Override
    @Transactional
    public List<AssignmentResponse> assignExamToStudents(AssignExamRequest request) {
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Examen con ID " + request.getExamId() + " no encontrado."));

        List<AssignmentResponse> createdAssignments = new ArrayList<>();

        for (Long studentId : request.getStudentIds()) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante con ID " + studentId + " no encontrado."));

            // El constructor de Assignment ahora usa Instant.now()
            Assignment assignment = new Assignment(student, exam);
            assignment.setCompleted(false);

            Assignment savedAssignment = assignmentRepository.save(assignment);
            createdAssignments.add(mapToAssignmentResponse(savedAssignment));
        }
        return createdAssignments;
    }

    @Override
    @Transactional
    public AssignmentResponse gradeAssignment(Long assignmentId, Double score) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación con ID " + assignmentId + " no encontrada."));

        if (!assignment.isCompleted()) {
            throw new IllegalStateException("La asignación aún no ha sido completada.");
        }
        if (score == null || score < 0) {
            throw new ValidationException("El puntaje no puede ser nulo o negativo.");
        }

        assignment.setScore(score);
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return mapToAssignmentResponse(updatedAssignment);
    }

    private AssignmentResponse mapToAssignmentResponse(Assignment assignment) {
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());

        // ✅ CORRECCIÓN 3: No se necesita conversión. Instant (Entidad) -> Instant (DTO)
        response.setAssignedDate(assignment.getAssignedDate());

        response.setCompleted(assignment.isCompleted());

        // ✅ CORRECCIÓN 4: No se necesita conversión. Instant (Entidad) -> Instant (DTO)
        response.setCompletionDate(assignment.getCompletionDate());

        response.setScore(assignment.getScore());

        if (assignment.getStudent() != null) {
            response.setStudentId(assignment.getStudent().getId());
            response.setStudentName(assignment.getStudent().getName());
        }
        if (assignment.getExam() != null) {
            response.setExamId(assignment.getExam().getId());
            response.setExamTitle(assignment.getExam().getTitle());
        }
        return response;
    }
}