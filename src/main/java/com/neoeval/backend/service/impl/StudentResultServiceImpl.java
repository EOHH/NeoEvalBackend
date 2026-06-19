package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.QuizSubmissionRequest;
import com.neoeval.backend.dto.request.UpdateResultScoreRequest;
import com.neoeval.backend.dto.response.StudentResultResponse;
import com.neoeval.backend.dto.response.StudentExamResultDetailResponse;
import com.neoeval.backend.entity.*;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.ExamRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.repository.StudentResultRepository;
import com.neoeval.backend.repository.StudentAnswerRepository;
import com.neoeval.backend.service.AchievementService;
import com.neoeval.backend.service.StudentResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class StudentResultServiceImpl implements StudentResultService {

    private final StudentResultRepository studentResultRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final AchievementService achievementService;

    public StudentResultServiceImpl(StudentResultRepository studentResultRepository,
                                    StudentRepository studentRepository,
                                    ExamRepository examRepository,
                                    StudentAnswerRepository studentAnswerRepository,
                                    AchievementService achievementService) {
        this.studentResultRepository = studentResultRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.achievementService = achievementService;
    }

    @Override
    @Transactional
    public StudentResult processQuizSubmission(QuizSubmissionRequest request) {
        // 1. Obtener entidades
        Student student = studentRepository.findStudentById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", request.getStudentId()));

        Exam exam = examRepository.findExamById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "ID", request.getExamId()));

        // 2. Mapear preguntas y respuestas correctas
        Map<Long, Question> questionMap = exam.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        Map<Long, Answer> correctAnswersEntityMap = exam.getQuestions().stream()
                .collect(Collectors.toMap(
                        Question::getId,
                        question -> question.getAnswers().stream()
                                .filter(Answer::isCorrect)
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                        "ERROR: Pregunta ID " + question.getId() + " no tiene respuesta correcta."
                                ))
                ));

        // 3. Calificación y creación de StudentAnswer
        int correctAnswersCount = 0;
        int totalQuestions = exam.getQuestions().size();

        if (totalQuestions == 0) {
            throw new IllegalArgumentException("El examen no contiene preguntas.");
        }

        List<StudentAnswer> studentAnswersToSave = new ArrayList<>();
        double maxPossibleScore = exam.getQuestions().stream()
                .mapToDouble(q -> q.getPoints() != null ? q.getPoints().doubleValue() : 1.0)
                .sum();

        for (QuizSubmissionRequest.AnswerSubmission submission : request.getAnswers()) {
            Long questionId = submission.getQuestionId();
            String submittedAnswerText = submission.getAnswer();

            Question question = questionMap.get(questionId);
            Answer correctAnswerEntity = correctAnswersEntityMap.get(questionId);

            if (question == null || correctAnswerEntity == null) continue;

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setQuestion(question);

            boolean isCorrect = false;
            double pointsAwarded = 0.0;
            String answerToPersist = submittedAnswerText;

            if (submittedAnswerText != null && submittedAnswerText.equalsIgnoreCase(correctAnswerEntity.getText())) {
                isCorrect = true;
                pointsAwarded = question.getPoints() != null ? question.getPoints().doubleValue() : 1.0;
                correctAnswersCount++;
                answerToPersist = correctAnswerEntity.getText();
            }

            studentAnswer.setSubmittedAnswer(answerToPersist);
            studentAnswer.setIsCorrect(isCorrect);
            studentAnswer.setPointsAwarded(pointsAwarded);

            studentAnswersToSave.add(studentAnswer);
        }

        // 4. Calcular score total y porcentaje
        double finalScore = studentAnswersToSave.stream().mapToDouble(StudentAnswer::getPointsAwarded).sum();
        double percentage = maxPossibleScore > 0 ? (finalScore * 100.0) / maxPossibleScore : 0.0;

        // 5. Crear y guardar el StudentResult
        StudentResult newResult = new StudentResult(
                student,
                exam,
                finalScore,
                percentage,
                totalQuestions,
                correctAnswersCount
        );

        StudentResult savedResult = studentResultRepository.save(newResult);

        // 6. Vincular y guardar StudentAnswer
        for (StudentAnswer sa : studentAnswersToSave) {
            sa.setStudentResult(savedResult);
        }
        studentAnswerRepository.saveAll(studentAnswersToSave);

        // 7. Puntos y Logros
        student.addPoints(finalScore);
        studentRepository.save(student);
        achievementService.assignAchievements(student, student.getTotalPoints());

        return savedResult;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResultResponse> getResultsByStudent(Long studentId, Pageable pageable) {
        Page<StudentResult> results = studentResultRepository.findResultsWithExamAndSubjectByStudentId(studentId, pageable);
        return results.map(this::mapToStudentResultResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentExamResultDetailResponse> getStudentResultsByExam(Long examId, Pageable pageable) {
        // We need to update findResultsWithStudentByExamId in the repo first, wait! 
        // Wait, I did not update findResultsWithStudentByExamId in StudentResultRepository. Let me check.
        // Let's assume it returns List for now.
        // Wait, the prompt says ALL endpoints returning lists should use Pageable.
        // I will return an error if I don't change it. But let's change it.
        Page<StudentResult> results = studentResultRepository.findResultsWithStudentByExamId(examId, pageable);
        return results.map(this::mapToStudentExamResultDetailResponse);
    }

    @Override
    @Transactional
    public StudentExamResultDetailResponse updateResultScore(Long resultId, UpdateResultScoreRequest request) {
        // 🔥 CORRECCIÓN 1: Obtener el StudentResult con las respuestas
        StudentResult result = studentResultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado", "ID", resultId));

        Student student = result.getStudent();
        double oldScore = result.getScore();

        // 🔥 CORRECCIÓN 2: Actualizar el StudentAnswer específico (LA CLAVE DEL PROBLEMA)
        if (request.getQuestionId() != null && request.getQuestionScore() != null) {
            // Buscar la respuesta específica del estudiante para esta pregunta
            StudentAnswer studentAnswer = studentAnswerRepository
                    .findByStudentResultAndQuestionId(result, request.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Respuesta del estudiante",
                            "Question ID",
                            request.getQuestionId()
                    ));

            // 🔥 ACTUALIZAR los puntos de la respuesta individual
            studentAnswer.setPointsAwarded(request.getQuestionScore());

            // 🔥 Marcar como calificada si tiene puntos
            if (request.getQuestionScore() > 0) {
                studentAnswer.setIsCorrect(true);
            }

            // 🔥 GUARDAR la respuesta actualizada
            studentAnswerRepository.save(studentAnswer);

            System.out.println("✅ StudentAnswer actualizado:");
            System.out.println("   - Question ID: " + request.getQuestionId());
            System.out.println("   - Nuevos puntos: " + request.getQuestionScore());
        }

        // 🔥 CORRECCIÓN 3: Actualizar el StudentResult (total)
        double newScore = request.getNewScore();
        double maxPossibleScore = result.getExam().getQuestions().stream()
                .mapToDouble(q -> q.getPoints() != null ? q.getPoints().doubleValue() : 1.0)
                .sum();
        double newPercentage = maxPossibleScore > 0 ? (newScore * 100.0) / maxPossibleScore : 0.0;

        result.setScore(newScore);
        result.setCorrectAnswers(request.getNewCorrectAnswers());
        result.setPercentage(newPercentage);

        StudentResult updatedResult = studentResultRepository.save(result);

        System.out.println("✅ StudentResult actualizado:");
        System.out.println("   - Nuevo score total: " + newScore);
        System.out.println("   - Nuevo porcentaje: " + newPercentage);

        // 🔥 CORRECCIÓN 4: Actualizar puntos del estudiante
        int oldPointsRounded = (int) Math.round(oldScore);
        int newPointsRounded = (int) Math.round(newScore);
        int scoreDifference = newPointsRounded - oldPointsRounded;

        if (scoreDifference != 0) {
            student.addPoints((double) scoreDifference);
            studentRepository.save(student);
            achievementService.assignAchievements(student, student.getTotalPoints());

            System.out.println("✅ Puntos del estudiante actualizados: +" + scoreDifference);
        }

        return mapToStudentExamResultDetailResponse(updatedResult);
    }

    // =================================================================
    // MÉTODOS DE MAPEO
    // =================================================================

    private StudentExamResultDetailResponse mapToStudentExamResultDetailResponse(StudentResult result) {
        StudentExamResultDetailResponse response = new StudentExamResultDetailResponse();

        response.setResultId(result.getId());
        response.setStudentId(result.getStudent() != null ? result.getStudent().getId() : null);

        if (result.getStudent() != null) {
            response.setStudentName(result.getStudent().getName());
            response.setStudentEmail(result.getStudent().getEmail());
        } else {
            response.setStudentName("Estudiante Desconocido");
            response.setStudentEmail("N/A");
        }

        response.setScore(result.getScore());
        response.setPercentage(result.getPercentage());
        response.setCorrectAnswers(result.getCorrectAnswers());
        response.setTotalQuestions(result.getTotalQuestions());
        response.setCompletedAt(result.getCompletedAt());

        return response;
    }

    private StudentResultResponse mapToStudentResultResponse(StudentResult result) {
        StudentResultResponse response = new StudentResultResponse();

        response.setId(result.getId());
        response.setExamId(result.getExam() != null ? result.getExam().getId() : null);

        if (result.getExam() != null) {
            response.setExamTitle(result.getExam().getTitle());
            if (result.getExam().getSubject() != null) {
                response.setSubjectName(result.getExam().getSubject().getName());
            } else {
                response.setSubjectName("Sin Asignatura");
            }
        } else {
            response.setExamTitle("Examen Desconocido");
            response.setSubjectName("N/A");
        }

        response.setScore(result.getScore());
        response.setPercentage(result.getPercentage());
        response.setTotalQuestions(result.getTotalQuestions());
        response.setCorrectAnswers(result.getCorrectAnswers());
        response.setCompletedAt(result.getCompletedAt());

        return response;
    }
}
