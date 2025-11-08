package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.TeacherResponse;
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.entity.ClassGroup;
import com.neoeval.backend.entity.Teacher;
import com.neoeval.backend.entity.Exam;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.TeacherRepository;
import com.neoeval.backend.repository.ClassGroupRepository;
import com.neoeval.backend.repository.ExamRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime; // Se mantiene por si se usa en otros mappers (ej: ExamResponse)

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassGroupRepository classGroupRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;


    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            ClassGroupRepository classGroupRepository,
            ExamRepository examRepository,
            StudentRepository studentRepository
    ) {
        this.teacherRepository = teacherRepository;
        this.classGroupRepository = classGroupRepository;
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", id));
        return mapToTeacherResponse(teacher);
    }

    @Override
    public List<TeacherResponse> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(this::mapToTeacherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassGroupResponse> getGroupsByTeacher(Long teacherId) {
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", teacherId));

        List<ClassGroup> classGroups = classGroupRepository.findByTeacher_Id(teacherId);
        // Usamos mapToGroupResponse, que se corrige a continuación
        return classGroups.stream()
                .map(this::mapToGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getExamsByTeacher(Long teacherId) {
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", teacherId));

        List<Exam> exams = examRepository.findByTeacher_Id(teacherId);
        return exams.stream()
                .map(this::mapToExamResponse)
                .collect(Collectors.toList());
    }

    // --- Métodos de Mapeo (Helper Methods) para TeacherServiceImpl ---

    private ClassGroupResponse mapToClassGroupResponse(ClassGroup classGroup) {
        if (classGroup == null) return null;
        ClassGroupResponse response = new ClassGroupResponse();

        response.setId(classGroup.getId());
        response.setName(classGroup.getName());
        response.setEducationalLevel(classGroup.getEducationalLevel());
        response.setDescription(classGroup.getDescription());

        // ✅ CORRECCIÓN 1: La entidad ClassGroup ahora devuelve Instant, se asigna directamente.
        if (classGroup.getCreatedAt() != null) {
            response.setCreatedAt(classGroup.getCreatedAt());
        }

        if (classGroup.getTeacher() != null) {
            response.setTeacherId(classGroup.getTeacher().getId());
            response.setTeacherName(classGroup.getTeacher().getName());
        }

        response.setStudentCount(0);
        response.setExamCount(0);

        return response;
    }

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse(teacher); // ✅ Constructor con User

        Long groupsCount = classGroupRepository.countByTeacher_Id(teacher.getId());
        Long examsCount = examRepository.countByTeacher_Id(teacher.getId());
        Long studentsCount = studentRepository.countStudentsByGroupTeacherId(teacher.getId());

        response.setGroupsCreated(groupsCount != null ? groupsCount.intValue() : 0);
        response.setExamsCreated(examsCount != null ? examsCount.intValue() : 0);
        response.setStudentsTaught(studentsCount != null ? studentsCount.intValue() : 0);

        return response;
    }

    private ClassGroupResponse mapToGroupResponse(ClassGroup classGroup) {
        ClassGroupResponse response = new ClassGroupResponse();
        response.setId(classGroup.getId());
        response.setName(classGroup.getName());
        if (classGroup.getDescription() != null) {
            response.setDescription(classGroup.getDescription());
        }
        if (classGroup.getEducationalLevel() != null) {
            response.setEducationalLevel(classGroup.getEducationalLevel());
        }

        // ✅ CORRECCIÓN 2: La entidad ClassGroup ahora devuelve Instant, se asigna directamente.
        if (classGroup.getCreatedAt() != null) {
            response.setCreatedAt(classGroup.getCreatedAt());
        }

        if (classGroup.getTeacher() != null) {
            response.setTeacherId(classGroup.getTeacher().getId());
            response.setTeacherName(classGroup.getTeacher().getName());
        } else {
            response.setTeacherId(null);
            response.setTeacherName("N/A");
        }

        return response;
    }

    private ExamResponse mapToExamResponse(Exam exam) {
        ExamResponse response = new ExamResponse();
        ZoneId zoneId = ZoneOffset.UTC;

        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        if (exam.getDescription() != null) {
            response.setDescription(exam.getDescription());
        }
        if (exam.getExamType() != null) {
            response.setExamType(exam.getExamType());
        }

        // Conversión de Instant (Entity) a LocalDateTime (DTO)
        // Esto es correcto, ya que Instant tiene atZone()
        if (exam.getOpeningDate() != null) {
            response.setOpeningDate(exam.getOpeningDate().atZone(zoneId).toLocalDateTime());
        }
        if (exam.getClosingDate() != null) {
            response.setClosingDate(exam.getClosingDate().atZone(zoneId).toLocalDateTime());
        }

        if (exam.getTimeLimitMinutes() != null) {
            response.setTimeLimitMinutes(exam.getTimeLimitMinutes());
        }
        if (exam.getAllowedAttempts() != null) {
            response.setAllowedAttempts(exam.getAllowedAttempts());
        }
        if (exam.getAverageDifficulty() != null) {
            response.setAverageDifficulty(exam.getAverageDifficulty());
        }

        if (exam.getTeacher() != null) {
            response.setTeacherId(exam.getTeacher().getId());
            response.setTeacherName(exam.getTeacher().getName());
        }

        if (exam.getSubject() != null) {
            response.setSubjectId(exam.getSubject().getId());
            response.setSubjectName(exam.getSubject().getName());
        }

        // ✅ Uso de mapToClassGroupResponse (ahora corregido)
        if (exam.getClassGroup() != null) {
            response.setClassGroup(mapToClassGroupResponse(exam.getClassGroup()));

        }

        return response;
    }
}