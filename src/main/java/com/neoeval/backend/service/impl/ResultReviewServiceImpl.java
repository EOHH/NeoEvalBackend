package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.response.QuizReviewResponse;
import com.neoeval.backend.dto.response.QuestionReviewDTO;
import com.neoeval.backend.entity.Answer;
import com.neoeval.backend.entity.Question;
import com.neoeval.backend.entity.StudentResult;
import com.neoeval.backend.entity.StudentAnswer;
import com.neoeval.backend.repository.StudentResultRepository;
import com.neoeval.backend.repository.StudentAnswerRepository;
import com.neoeval.backend.service.ResultReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultReviewServiceImpl implements ResultReviewService {

    private final StudentResultRepository studentResultRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    public ResultReviewServiceImpl(StudentResultRepository studentResultRepository, StudentAnswerRepository studentAnswerRepository) {
        this.studentResultRepository = studentResultRepository;
        this.studentAnswerRepository = studentAnswerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public QuizReviewResponse getReviewDetails(Long resultId) {
        StudentResult result = studentResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Resultado de quiz no encontrado con ID: " + resultId));

        // 1. Obtener todas las respuestas del estudiante para este resultado
        // Se asume que studentAnswerRepository.findByStudentResultId usa JOIN FETCH para traer Question y Answers
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByStudentResultId(resultId);

        // Mapear las respuestas del estudiante para acceso rápido por Question ID
        Map<Long, StudentAnswer> answerMap = studentAnswers.stream()
                .collect(Collectors.toMap(sa -> sa.getQuestion().getId(), sa -> sa));

        // 2. Procesar las preguntas del examen para crear el DTO de revisión
        List<QuestionReviewDTO> reviewQuestions = result.getExam().getQuestions().stream()
                .map(question -> buildQuestionReviewDTO(question, answerMap.get(question.getId())))
                .collect(Collectors.toList());

        // 3. Ensamblar el DTO de Respuesta final
        double maxScore = 20.0; // Mantener asunción de puntaje máximo

        return new QuizReviewResponse(
                result.getId(),
                result.getExam().getId(),
                result.getExam().getTitle(),
                result.getScore(),
                maxScore,
                result.getCorrectAnswers(),
                result.getPercentage(),
                reviewQuestions
        );
    }

    // Método auxiliar para construir el DTO de detalle de la pregunta
    private QuestionReviewDTO buildQuestionReviewDTO(Question question, StudentAnswer studentAnswer) {

        // Puntos Máximos de la Pregunta
        Double maxQuestionPoints = (question.getPoints() != null ? question.getPoints().doubleValue() : 0.0);

        // Encontrar la respuesta correcta (Answer.isCorrect() de la entidad Answer)
        Answer correctAnswerEntity = question.getAnswers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElse(null);

        // La respuesta correcta oficial (el texto de la opción)
        String correctAnswerText = correctAnswerEntity != null ? correctAnswerEntity.getText() : "N/A";

        // Determinar si es correcta usando el valor de la base de datos (isCorrect en StudentAnswer)
        Boolean isCorrect = studentAnswer != null ? studentAnswer.getIsCorrect() : false;

        // Determinar los puntos otorgados (obtenidos)
        Double pointsAwarded = (studentAnswer != null && studentAnswer.getPointsAwarded() != null)
                ? studentAnswer.getPointsAwarded()
                : 0.0;

        // -------------------------------------------------------------
        // 🟢 FIX 1: Lógica de la respuesta del estudiante
        // -------------------------------------------------------------
        String userAnswerText = "No respondida";

        if (studentAnswer != null && studentAnswer.getSubmittedAnswer() != null && !studentAnswer.getSubmittedAnswer().isBlank()) {
            // Se usa la respuesta guardada en StudentAnswer
            userAnswerText = studentAnswer.getSubmittedAnswer();
        } else if (studentAnswer == null) {
            userAnswerText = "No respondida";
        } else {
            // Caso de respuestas de opción múltiple donde solo se guardó 'isCorrect' pero no el texto,
            // o si el texto guardado era nulo/vacío
            userAnswerText = "Respuesta marcada (dato no capturado)";
        }

        // -------------------------------------------------------------
        // 🟢 FIX 2: Lógica de preguntas abiertas (Requiere calificación manual)
        // Si es OPEN_ENDED, isCorrect debe ser NULL para que el frontend lo detecte como 'pendiente'.
        // -------------------------------------------------------------
        if ("OPEN_ENDED".equals(question.getQuestionType())) {
            // Para preguntas abiertas, el estado inicial DEBE ser null/pendiente.
            // Si el profesor ya la calificó, el score será > 0 y el frontend lo mostrará.
            // Si el score es 0.0 (como en tu ejemplo), debe marcarse como pendiente si es abierta.

            // Si el score es 0.0, asumimos que no ha sido calificada y forzamos 'null'
            if (pointsAwarded.equals(0.0)) {
                isCorrect = null; // Fuerza el estado de pendiente
            } else {
                // Si el score es > 0, asumimos que fue calificada y usamos el isCorrect de la DB, o TRUE si es > 0
                isCorrect = pointsAwarded.equals(maxQuestionPoints);
            }

            // La respuesta correcta en preguntas abiertas es irrelevante para el estudiante
            correctAnswerText = "Se requiere revisión manual del profesor.";
        }

        // -------------------------------------------------------------

        return new QuestionReviewDTO(
                question.getId(),
                question.getQuestionText(),
                question.getQuestionType(),
                maxQuestionPoints,      // ⬅️ maxPoints
                userAnswerText,
                correctAnswerText,
                isCorrect,              // ⬅️ Puede ser TRUE, FALSE o NULL (para pendiente)
                question.getExplanation(),
                pointsAwarded           // ⬅️ obtainedScore
        );
    }
}