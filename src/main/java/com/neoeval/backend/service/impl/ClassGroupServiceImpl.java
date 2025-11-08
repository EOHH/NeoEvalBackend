package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.CreateGroupRequest;
import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.entity.ClassGroup;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.entity.Teacher;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.exception.ValidationException;
import com.neoeval.backend.repository.ClassGroupRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.repository.TeacherRepository;
import com.neoeval.backend.service.ClassGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
// Importaciones necesarias para la conversión de fechas
import java.time.ZoneOffset;

@Service
public class ClassGroupServiceImpl implements ClassGroupService {

    private final ClassGroupRepository classGroupRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public ClassGroupServiceImpl(
            ClassGroupRepository classGroupRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository
    ) {
        this.classGroupRepository = classGroupRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public ClassGroupResponse createGroup(CreateGroupRequest groupRequest) {
        Teacher teacher = teacherRepository.findById(groupRequest.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", groupRequest.getTeacherId()));

        ClassGroup classGroup = new ClassGroup();
        classGroup.setName(groupRequest.getName());
        classGroup.setDescription(groupRequest.getDescription());
        classGroup.setEducationalLevel(groupRequest.getEducationalLevel());
        classGroup.setTeacher(teacher);

        ClassGroup savedClassGroup = classGroupRepository.save(classGroup);
        return mapToGroupResponse(savedClassGroup);
    }

    @Override
    public ClassGroupResponse getGroupById(Long id) {
        ClassGroup classGroup = classGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));
        return mapToGroupResponse(classGroup);
    }

    @Override
    public List<ClassGroupResponse> getAllGroups() {
        return classGroupRepository.findAll().stream()
                .map(this::mapToGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassGroupResponse> getGroupsByTeacherId(Long teacherId) {
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", teacherId));

        List<ClassGroup> classGroups = classGroupRepository.findByTeacher_Id(teacherId);
        return classGroups.stream()
                .map(this::mapToGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClassGroupResponse addStudentsToGroup(Long groupId, List<Long> studentIds) {
        ClassGroup classGroup = classGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", groupId));

        List<Student> studentsToAdd = studentRepository.findAllById(studentIds);

        List<Student> newStudents = studentsToAdd.stream()
                .filter(student -> !classGroup.getStudents().contains(student))
                .collect(Collectors.toList());

        if (newStudents.isEmpty() && !studentIds.isEmpty()) {
            throw new ValidationException("Todos los estudiantes seleccionados ya pertenecen a este grupo o los IDs no son válidos.");
        }

        newStudents.forEach(student -> {
            classGroup.getStudents().add(student);
            student.addClassGroup(classGroup);
        });

        classGroupRepository.save(classGroup);
        studentRepository.saveAll(newStudents);

        return mapToGroupResponse(classGroup);
    }

    @Override
    @Transactional
    public ClassGroupResponse addStudentToGroup(Long groupId, Long studentId) {
        ClassGroup classGroup = classGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", groupId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        if (classGroup.getStudents().contains(student)) {
            throw new ValidationException("El estudiante con ID " + studentId + " ya está en el grupo " + groupId + ".");
        }

        classGroup.getStudents().add(student);
        student.addClassGroup(classGroup);

        classGroupRepository.save(classGroup);
        studentRepository.save(student);
        return mapToGroupResponse(classGroup);
    }

    @Override
    @Transactional
    public ClassGroupResponse removeStudentFromGroup(Long groupId, Long studentId) {
        ClassGroup classGroup = classGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", groupId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        if (!classGroup.getStudents().contains(student)) {
            throw new ValidationException("El estudiante con ID " + studentId + " no está en el grupo " + groupId + ".");
        }

        classGroup.getStudents().remove(student);
        student.removeClassGroup(classGroup);

        classGroupRepository.save(classGroup);
        studentRepository.save(student);
        return mapToGroupResponse(classGroup);
    }

    @Override
    @Transactional
    public ClassGroupResponse updateGroup(Long id, CreateGroupRequest groupRequest) {
        ClassGroup existingClassGroup = classGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", id));

        existingClassGroup.setName(groupRequest.getName());
        existingClassGroup.setDescription(groupRequest.getDescription());
        existingClassGroup.setEducationalLevel(groupRequest.getEducationalLevel());

        if (groupRequest.getTeacherId() != null &&
                !existingClassGroup.getTeacher().getId().equals(groupRequest.getTeacherId())) {
            Teacher newTeacher = teacherRepository.findById(groupRequest.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", groupRequest.getTeacherId()));
            existingClassGroup.setTeacher(newTeacher);
        }

        ClassGroup updatedClassGroup = classGroupRepository.save(existingClassGroup);
        return mapToGroupResponse(updatedClassGroup);
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        if (!classGroupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grupo", "id", id);
        }

        ClassGroup group = classGroupRepository.findById(id).orElse(null);
        if (group != null) {
            new java.util.ArrayList<>(group.getStudents()).forEach(student -> student.removeClassGroup(group));
            studentRepository.saveAll(group.getStudents());
        }

        classGroupRepository.deleteById(id);
    }

    private ClassGroupResponse mapToGroupResponse(ClassGroup classGroup) {
        ClassGroupResponse response = new ClassGroupResponse();
        response.setId(classGroup.getId());
        response.setName(classGroup.getName());
        response.setDescription(classGroup.getDescription());

        // ✅ Asignar createdAt (Instant)
        if (classGroup.getCreatedAt() != null) {
            response.setCreatedAt(classGroup.getCreatedAt());
        }

        response.setEducationalLevel(classGroup.getEducationalLevel() != null
                ? classGroup.getEducationalLevel()
                : "No especificado");

        if (classGroup.getTeacher() != null) {
            response.setTeacherId(classGroup.getTeacher().getId());
            response.setTeacherName(classGroup.getTeacher().getName());
        } else {
            response.setTeacherId(null);
            response.setTeacherName("N/A");
        }

        // ✅ CORRECCIÓN CLAVE: Usar el nuevo constructor con Student (User)
        if (classGroup.getStudents() != null && !classGroup.getStudents().isEmpty()) {
            List<StudentResponse> studentResponses = classGroup.getStudents().stream()
                    .map(student -> {
                        // Crear StudentResponse con el constructor automático
                        StudentResponse studentResp = new StudentResponse(student);

                        // Agregar campos específicos de Student
                        studentResp.setEducationalLevel(student.getEducationalLevel());
                        studentResp.setBirthDate(student.getBirthDate());
                        studentResp.setTotalPoints(student.getTotalPoints());
                        studentResp.setGamificationLevel(student.getGamificationLevel());

                        // Agregar información del padre si existe
                        if (student.getParent() != null) {
                            studentResp.setParentId(student.getParent().getId());
                            studentResp.setParentName(student.getParent().getName());
                        }

                        // Valores por defecto para exámenes y certificados
                        studentResp.setExamCompleted(0);
                        studentResp.setCertificatesEarned(0);

                        return studentResp;
                    })
                    .collect(Collectors.toList());

            response.setStudents(studentResponses);
            response.setStudentCount(studentResponses.size());
        } else {
            response.setStudents(Collections.emptyList());
            response.setStudentCount(0);
        }

        return response;
    }
}