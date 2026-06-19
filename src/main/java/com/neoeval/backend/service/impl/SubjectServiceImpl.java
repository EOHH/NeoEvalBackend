package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.CreateSubjectRequest;
import com.neoeval.backend.dto.response.SubjectResponse;
import com.neoeval.backend.entity.Subject;
import com.neoeval.backend.entity.User;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.SubjectRepository;
import com.neoeval.backend.repository.UserRepository;
import com.neoeval.backend.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; // Importación corregida a Instant
import java.time.LocalDateTime; // Se mantiene para el DTO
import java.time.ZoneId; // Nuevo import para la conversión
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> getAllSubjects(Pageable pageable) {
        return subjectRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con id: " + id));
        return mapToResponse(subject);
    }

    @Override
    @Transactional
    public SubjectResponse createSubject(CreateSubjectRequest request) {
        // Validar nombre único
        if (subjectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Ya existe una asignatura con ese nombre");
        }

        // Validar código único (si se proporciona)
        if (request.getCode() != null && !request.getCode().isBlank() &&
                subjectRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ya existe una asignatura con ese código");
        }

        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDescription(request.getDescription());
        subject.setEducationalLevel(request.getEducationalLevel());
        subject.setCredits(request.getCredits());
        subject.setHoursPerWeek(request.getHoursPerWeek());
        subject.setSemester(request.getSemester());

        // ✅ CORRECCIÓN: Usar Instant.now() para la entidad (soluciona errores 1 y 2)
        subject.setIsActive(true);
        subject.setCreatedAt(Instant.now());
        subject.setUpdatedAt(Instant.now());

        Subject savedSubject = subjectRepository.save(subject);
        return mapToResponse(savedSubject);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(Long id, CreateSubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con id: " + id));

        // Validar nombre único (si cambió)
        if (!subject.getName().equals(request.getName()) &&
                subjectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Ya existe una asignatura con ese nombre");
        }

        // Validar código único (si cambió)
        if (request.getCode() != null && !request.getCode().isBlank() &&
                !request.getCode().equals(subject.getCode()) &&
                subjectRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ya existe una asignatura con ese código");
        }

        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDescription(request.getDescription());
        subject.setEducationalLevel(request.getEducationalLevel());
        subject.setCredits(request.getCredits());
        subject.setHoursPerWeek(request.getHoursPerWeek());
        subject.setSemester(request.getSemester());

        // Asignar Instant.now() para updatedAt si la auditoría no es automática
        subject.setUpdatedAt(Instant.now());


        Subject updatedSubject = subjectRepository.save(subject);
        return mapToResponse(updatedSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con id: " + id));

        // Soft delete en lugar de eliminación física
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    // Los demás métodos permanecen igual...
    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> getSubjectsByTeacher(Long teacherId, Pageable pageable) {
        if (!userRepository.existsById(teacherId)) {
            throw new ResourceNotFoundException("Profesor no encontrado con id: " + teacherId);
        }

        return subjectRepository.findSubjectsByTeacherId(teacherId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> getSubjectsByStudent(Long studentId, Pageable pageable) {
        // Implementación simplificada
        return subjectRepository.findByIsActive(true, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectResponse> searchSubjects(String query, Pageable pageable) {
        return subjectRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public SubjectResponse assignTeacherToSubject(Long subjectId, Long teacherId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado"));

        if (!"TEACHER".equals(teacher.getUserType())) {
            throw new IllegalArgumentException("El usuario no es un profesor");
        }

        if (!subject.getTeachers().contains(teacher)) {
            subject.addTeacher(teacher); // Usar el helper method
        }

        Subject updatedSubject = subjectRepository.save(subject);
        return mapToResponse(updatedSubject);
    }

    @Override
    @Transactional
    public void removeTeacherFromSubject(Long subjectId, Long teacherId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada"));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado"));

        if (subject.getTeachers().contains(teacher)) {
            subject.removeTeacher(teacher); // Usar el helper method
            subjectRepository.save(subject);
        }
    }

    private SubjectResponse mapToResponse(Subject subject) {
        // 🛑 ¡ATENCIÓN! Ya no se necesitan ZoneId, LocalDateTime ni conversiones complejas.
        // Simplemente pasamos el Instant de la Entidad al Instant del DTO.

        return new SubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getCode(),
                subject.getDescription(),
                subject.getEducationalLevel(),
                subject.getCredits(),
                subject.getHoursPerWeek(),
                subject.getSemester(),
                subject.getIsActive(),

                subject.getCreatedAt(), // ✅ Directamente Instant (UTC)
                subject.getUpdatedAt()  // ✅ Directamente Instant (UTC)
        );
    }
}