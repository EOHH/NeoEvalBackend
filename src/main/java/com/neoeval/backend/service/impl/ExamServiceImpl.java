package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.CreateExamRequest;
import com.neoeval.backend.dto.response.ExamResponse;
import com.neoeval.backend.dto.response.ExamSummaryResponse;
import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.entity.*;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.*;
import com.neoeval.backend.service.ExamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private static final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;
    private final TeacherRepository teacherRepository;
    private final ClassGroupRepository classGroupRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final StudentResultRepository studentResultRepository;

    public ExamServiceImpl(
            ExamRepository examRepository,
            TeacherRepository teacherRepository,
            ClassGroupRepository classGroupRepository,
            SubjectRepository subjectRepository,
            StudentRepository studentRepository,
            StudentResultRepository studentResultRepository
    ) {
        this.examRepository = examRepository;
        this.teacherRepository = teacherRepository;
        this.classGroupRepository = classGroupRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.studentResultRepository = studentResultRepository;
    }

    // --- Mapeadores Auxiliares ---

    private ClassGroupResponse mapToClassGroupResponse(ClassGroup classGroup) {
        if (classGroup == null) return null;
        ClassGroupResponse response = new ClassGroupResponse();

        response.setId(classGroup.getId());
        response.setName(classGroup.getName());
        response.setEducationalLevel(classGroup.getEducationalLevel());
        response.setDescription(classGroup.getDescription());

        if (classGroup.getCreatedAt() != null) {
            response.setCreatedAt(classGroup.getCreatedAt().atZone(ZoneOffset.UTC).toInstant());
        }

        if (classGroup.getTeacher() != null) {
            response.setTeacherId(classGroup.getTeacher().getId());
            response.setTeacherName(classGroup.getTeacher().getName());
        }

        response.setStudentCount(0);
        response.setExamCount(0);

        return response;
    }

    private ExamResponse mapToExamResponse(Exam exam) {
        ZoneId zoneId = ZoneOffset.UTC;
        ExamResponse response = new ExamResponse();

        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        response.setDescription(exam.getDescription());
        response.setExamType(exam.getExamType());

        if (exam.getOpeningDate() != null) {
            response.setOpeningDate(exam.getOpeningDate());
        }
        if (exam.getClosingDate() != null) {
            response.setClosingDate(exam.getClosingDate());
        }

        response.setTimeLimitMinutes(exam.getTimeLimitMinutes());
        response.setAllowedAttempts(exam.getAllowedAttempts());
        response.setAverageDifficulty(exam.getAverageDifficulty());

        if (exam.getTeacher() != null) {
            response.setTeacherId(exam.getTeacher().getId());
            response.setTeacherName(exam.getTeacher().getName());
        } else {
            response.setTeacherId(null);
            response.setTeacherName("N/A");
        }

        if (exam.getClassGroup() != null) {
            response.setClassGroup(mapToClassGroupResponse(exam.getClassGroup()));
        }

        if (exam.getSubject() != null) {
            response.setSubjectId(exam.getSubject().getId());
            response.setSubjectName(exam.getSubject().getName());
        } else {
            response.setSubjectId(null);
            response.setSubjectName("N/A");
        }

        response.setIsCompleted(false);

        // 🚀 CORRECCIÓN: Contar las preguntas y asignarlas al DTO
        if (exam.getQuestions() != null) {
            response.setQuestionCount(exam.getQuestions().size());
        } else {
            response.setQuestionCount(0);
        }

        return response;
    }

    // ----------------------------------------------------------------------------------
    // RESTO DE MÉTODOS DEL SERVICIO
    // ----------------------------------------------------------------------------------

    @Override
    @Transactional
    public ExamResponse createExam(CreateExamRequest examRequest) {
        User teacher = teacherRepository.findById(examRequest.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", examRequest.getTeacherId()));

        Subject subject = subjectRepository.findById(examRequest.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Materia", "id", examRequest.getSubjectId()));

        Exam exam = new Exam();
        exam.setTitle(examRequest.getTitle());
        exam.setDescription(examRequest.getDescription());
        exam.setExamType(examRequest.getExamType());
        exam.setTeacher(teacher);
        exam.setSubject(subject);

        if (examRequest.getGroupId() != null) {
            ClassGroup classGroup = classGroupRepository.findById(examRequest.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", examRequest.getGroupId()));
            exam.setClassGroup(classGroup);
        }

        if (examRequest.getOpeningDate() != null) {
            exam.setOpeningDate(examRequest.getOpeningDate());
        }
        if (examRequest.getClosingDate() != null) {
            exam.setClosingDate(examRequest.getClosingDate());
        }

        exam.setTimeLimitMinutes(examRequest.getTimeLimitMinutes());
        exam.setAllowedAttempts(examRequest.getAllowedAttempts());
        exam.setAverageDifficulty(examRequest.getAverageDifficulty());

        Exam savedExam = examRepository.save(exam);
        return mapToExamResponse(savedExam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", id));
        return mapToExamResponse(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResponse> getExamsByGroup(Long groupId, Pageable pageable) {
        Page<Exam> exams = examRepository.findByClassGroup_Id(groupId, pageable);
        return exams.map(this::mapToExamResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResponse> getExamsByTeacher(Long teacherId, Pageable pageable) {
        Page<Exam> exams = examRepository.findByTeacher_Id(teacherId, pageable);
        return exams.map(this::mapToExamResponse);
    }

    @Override
    @Transactional
    public ExamResponse updateExam(Long id, CreateExamRequest examRequest) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", id));

        if (examRequest.getTitle() != null) exam.setTitle(examRequest.getTitle());
        if (examRequest.getDescription() != null) exam.setDescription(examRequest.getDescription());
        if (examRequest.getExamType() != null) exam.setExamType(examRequest.getExamType());

        if (examRequest.getTeacherId() != null) {
            if (exam.getTeacher() == null || !examRequest.getTeacherId().equals(exam.getTeacher().getId())) {
                User newTeacher = teacherRepository.findById(examRequest.getTeacherId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", examRequest.getTeacherId()));
                exam.setTeacher(newTeacher);
            }
        }

        if (examRequest.getGroupId() != null) {
            if (exam.getClassGroup() == null || !examRequest.getGroupId().equals(exam.getClassGroup().getId())) {
                ClassGroup newClassGroup = classGroupRepository.findById(examRequest.getGroupId())
                        .orElseThrow(() -> new ResourceNotFoundException("Grupo", "id", examRequest.getGroupId()));
                exam.setClassGroup(newClassGroup);
            }
        } else {
            exam.setClassGroup(null);
        }

        if (examRequest.getSubjectId() != null) {
            if (exam.getSubject() == null || !examRequest.getSubjectId().equals(exam.getSubject().getId())) {
                Subject newSubject = subjectRepository.findById(examRequest.getSubjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Materia", "id", examRequest.getSubjectId()));
                exam.setSubject(newSubject);
            }
        }

        if (examRequest.getOpeningDate() != null) exam.setOpeningDate(examRequest.getOpeningDate());
        if (examRequest.getClosingDate() != null) exam.setClosingDate(examRequest.getClosingDate());

        if (examRequest.getTimeLimitMinutes() != null) exam.setTimeLimitMinutes(examRequest.getTimeLimitMinutes());
        if (examRequest.getAllowedAttempts() != null) exam.setAllowedAttempts(examRequest.getAllowedAttempts());
        if (examRequest.getAverageDifficulty() != null) exam.setAverageDifficulty(examRequest.getAverageDifficulty());

        Exam updatedExam = examRepository.save(exam);
        return mapToExamResponse(updatedExam);
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", id));
        examRepository.delete(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResponse> getAvailableExamsForStudent(Long studentId, Pageable pageable) {
        User studentUser = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        if (!(studentUser instanceof Student)) {
            log.warn("Usuario con ID {} no es un Estudiante, sino un {}", studentId, studentUser.getClass().getSimpleName());
            throw new ResourceNotFoundException("Estudiante", "id", studentId);
        }
        Student student = (Student) studentUser;

        Set<ClassGroup> groups = student.getClassGroups();
        if (groups == null || groups.isEmpty()) {
            log.info("DEBUG Quizzes (ID: {}): El estudiante no pertenece a ningún grupo. Retornando lista vacía.", studentId);
            return Page.empty();
        }

        List<Long> classGroupIds = groups.stream()
                .map(ClassGroup::getId)
                .collect(Collectors.toList());

        log.info("DEBUG Quizzes (ID: {}): Buscando TODOS los exámenes asignados a los grupos: {}",
                studentId, classGroupIds);

        Page<Exam> assignedExams = examRepository.findAllAssignedExamsForStudent(
                classGroupIds, pageable
        );

        log.info("DEBUG Quizzes (ID: {}): Exámenes totales encontrados (Abiertos, Cerrados, Próximos): {}",
                studentId, assignedExams.getTotalElements());

        return assignedExams.map(this::mapToExamResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResponse> getStudentExamHistory(Long studentId, Pageable pageable) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        Page<StudentResult> completedResults = studentResultRepository.findResultsWithExamAndSubjectByStudentId(studentId, pageable);

        return completedResults.map(studentResult -> {
            Exam exam = studentResult.getExam();
            if (exam == null) {
                return null; // Will map to null inside the Page, might need a filter later if Spring Data allows
            }
            ExamResponse response = mapToExamResponse(exam);
            response.setIsCompleted(true);
            return response;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamSummaryResponse> getExamSummariesByTeacher(Long teacherId, Pageable pageable) {
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "id", teacherId));

        return studentResultRepository.findExamSummariesByTeacherId(teacherId, pageable);
    }
}