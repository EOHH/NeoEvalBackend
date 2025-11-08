package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.UserRequest; // 👈 ¡CORREGIDO! Usar UserRequest
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.entity.ClassGroup;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.ClassGroupRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.service.StudentAchievementService;
import com.neoeval.backend.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassGroupRepository classGroupRepository;
    private final StudentAchievementService studentAchievementService;

    // Constructor con todas las dependencias
    public StudentServiceImpl(
            StudentRepository studentRepository,
            ClassGroupRepository classGroupRepository,
            StudentAchievementService studentAchievementService) {
        this.studentRepository = studentRepository;
        this.classGroupRepository = classGroupRepository;
        this.studentAchievementService = studentAchievementService;
    }

    // --- 1. MÉTODOS CRUD (Usando UserRequest) ---

    @Override
    @Transactional
    public StudentResponse createStudent(UserRequest request) { // 👈 Firma actualizada
        // Mapeo de UserRequest a Student (la entidad Student maneja la herencia)
        Student student = new Student(request.getName(), request.getEmail(), request.getPassword());

        // Campos específicos de Student:
        student.setEducationalLevel(request.getEducationalLevel());
        student.setBirthDate(request.getBirthDate());

        // Aseguramos que el tipo de usuario sea 'STUDENT'
        student.setUserType("STUDENT");

        Student savedStudent = studentRepository.save(student);
        return mapToStudentResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, UserRequest request) { // 👈 Firma actualizada
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));

        // Actualizar campos básicos
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        // NO se recomienda actualizar la contraseña directamente aquí

        // Actualizar campos específicos de Student
        student.setEducationalLevel(request.getEducationalLevel());
        student.setBirthDate(request.getBirthDate());

        Student updatedStudent = studentRepository.save(student);
        return mapToStudentResponse(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante", "id", id);
        }
        studentRepository.deleteById(id);
    }

    // --- 2. MÉTODOS DE LECTURA (Sin cambios) ---

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        return mapToStudentResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> getStudentsByGroup(Long groupId) {
        ClassGroup classGroup = classGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup", "id", groupId));

        Set<Student> studentsSet = classGroup.getStudents();

        return studentsSet.stream()
                .map(this::mapToStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> searchStudentsByName(String name) {
        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        return students.stream()
                .map(this::mapToStudentResponse)
                .collect(Collectors.toList());
    }

    // --- 3. LÓGICA CLAVE DE GAMIFICACIÓN (Sin cambios en la lógica) ---

    @Override
    @Transactional
    public StudentResponse addPointsToStudent(Long studentId, double pointsToAdd) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        student.addPoints(pointsToAdd);
        Student updatedStudent = studentRepository.save(student);

        // Disparar la lógica de logros:
        studentAchievementService.assignAchievements(
                updatedStudent,
                updatedStudent.getTotalPoints()
        );

        return mapToStudentResponse(updatedStudent);
    }


    // --- 4. MÉTODO DE MAPEO (Sin cambios) ---

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse response = new StudentResponse(student); // ✅ Constructor con User

        response.setEducationalLevel(student.getEducationalLevel());
        response.setBirthDate(student.getBirthDate());
        response.setTotalPoints(student.getTotalPoints());
        response.setGamificationLevel(student.getGamificationLevel());

        if (student.getParent() != null) {
            response.setParentId(student.getParent().getId());
        }

        return response;
    }
}