package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.QuestionRequest;
import com.neoeval.backend.dto.response.QuestionResponse;
import com.neoeval.backend.entity.Answer;
import com.neoeval.backend.entity.Exam;
import com.neoeval.backend.entity.Question;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.ExamRepository;
import com.neoeval.backend.repository.QuestionRepository;
import com.neoeval.backend.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final ObjectMapper objectMapper;

    // Constructor para inyección de dependencias
    public QuestionServiceImpl(QuestionRepository questionRepository, ExamRepository examRepository, ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public QuestionResponse createQuestion(Long examId, QuestionRequest questionRequest) {
        // Busca el examen por su ID.
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", examId));

        // 1. Mapeo de DTO a Entidad Question
        Question question = new Question();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setQuestionType(questionRequest.getQuestionType());
        question.setDifficulty(questionRequest.getDifficulty());
        question.setPoints(questionRequest.getPoints());
        question.setOptions(questionRequest.getOptions());
        question.setCorrectAnswer(questionRequest.getCorrectAnswer());
        question.setExplanation(questionRequest.getExplanation());
        question.setExam(exam);

        // 🚨 2. Lógica CRÍTICA: Convertir opciones/respuesta clave en entidades Answer
        if ("MULTIPLE_CHOICE".equals(question.getQuestionType()) ||
                "TRUE_FALSE".equals(question.getQuestionType()) ||
                "OPEN_ENDED".equals(question.getQuestionType())) { // <-- INCLUIDO OPEN_ENDED

            Set<Answer> answers = processAnswers(question);

            // CRÍTICO: Usar el helper addAnswer para establecer la bidireccionalidad.
            for (Answer answer : answers) {
                question.addAnswer(answer); // <-- CORREGIDO para usar el helper
            }
        }

        // 3. Guardar la pregunta. Las respuestas se guardan en cascada.
        Question savedQuestion = questionRepository.save(question);

        return mapToQuestionResponse(savedQuestion);
    }

    @Override
    public QuestionResponse getQuestionById(Long id) {
        // Busca una pregunta por su ID.
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta", "id", id));
        return mapToQuestionResponse(question);
    }

    @Override
    public List<QuestionResponse> getAllQuestions() {
        // Obtiene todas las preguntas de la base de datos
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest questionRequest) {
        // Busca la pregunta por su ID.
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta", "id", id));

        // 1. Mapeo de campos del Request a la Entidad Question
        question.setQuestionText(questionRequest.getQuestionText());
        question.setQuestionType(questionRequest.getQuestionType());
        question.setDifficulty(questionRequest.getDifficulty());
        question.setPoints(questionRequest.getPoints());
        question.setOptions(questionRequest.getOptions());
        question.setCorrectAnswer(questionRequest.getCorrectAnswer());
        question.setExplanation(questionRequest.getExplanation());

        // 🚨 2. Lógica CRÍTICA para la ACTUALIZACIÓN:
        if ("MULTIPLE_CHOICE".equals(question.getQuestionType()) ||
                "TRUE_FALSE".equals(question.getQuestionType()) ||
                "OPEN_ENDED".equals(question.getQuestionType())) { // <-- INCLUIDO OPEN_ENDED

            // Paso A: Obtener las nuevas respuestas como un Set temporal
            Set<Answer> newAnswers = processAnswers(question);

            // Paso B: ELIMINAR las respuestas antiguas de la colección manejada por JPA.
            // Esto activa orphanRemoval=true y borra las filas de la DB.
            question.getAnswers().clear();

            // Paso C (CRÍTICO): Usar el helper 'addAnswer' para añadir las nuevas respuestas.
            for (Answer answer : newAnswers) {
                // Usamos el helper addAnswer de la entidad Question:
                question.addAnswer(answer);
            }
        } else {
            // Si el tipo de pregunta cambia a uno sin Answer (ej. un tipo nuevo que no usa Answer),
            // nos aseguramos de limpiar las respuestas antiguas por seguridad.
            question.getAnswers().clear();
        }


        // 3. Guarda la pregunta actualizada (JPA guarda en cascada las nuevas Answers)
        Question updatedQuestion = questionRepository.save(question);

        return mapToQuestionResponse(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        // Busca la pregunta por su ID.
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pregunta", "id", id));
        // Elimina la pregunta
        questionRepository.delete(question);
    }

    @Override
    public List<QuestionResponse> getQuestionsByExamId(Long examId) {
        // Asumiendo que QuestionRepository tiene un método findByExamId
        List<Question> questions = questionRepository.findByExamId(examId);

        return questions.stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    // 🚨 MÉTODO AUXILIAR PRINCIPAL: Convierte los campos JSON a Entidades Answer
    private Set<Answer> processAnswers(Question question) {
        Set<Answer> answers = new HashSet<>();
        String questionType = question.getQuestionType();

        String rawCorrectAnswer = question.getCorrectAnswer();
        if (rawCorrectAnswer == null || rawCorrectAnswer.isEmpty()) {
            // Permitimos esto solo si no es un tipo que requiere una respuesta clave obligatoria
            if ("OPEN_ENDED".equals(questionType) || "TRUE_FALSE".equals(questionType)) {
                throw new IllegalArgumentException("El campo 'correctAnswer' no debe ser nulo para este tipo de pregunta.");
            }
            return answers;
        }

        // --------------------------------------------------------
        // 0. Lógica para OPEN_ENDED (Pregunta Abierta)
        // --------------------------------------------------------
        if ("OPEN_ENDED".equals(questionType)) {
            try {
                // El frontend envía la respuesta como jsonEncode(String), resultando en: "\"palabra clave\""
                // ObjectMapper.readValue(String, String.class) lee el JSON string y devuelve el valor sin las comillas de escape.
                String correctText = objectMapper.readValue(rawCorrectAnswer, String.class);

                Answer answer = new Answer();
                answer.setText(correctText); // La palabra/frase clave
                answer.setIsCorrect(true);
                answer.setPointsValue(question.getPoints());
                // NO SE ESTABLECE answer.setQuestion(question) aquí. Lo hará question.addAnswer(answer).

                answers.add(answer);
                return answers;

            } catch (Exception e) {
                System.err.println("Error FATAL al procesar pregunta OPEN_ENDED: " + e.getMessage());
                throw new RuntimeException("Error en el formato de respuesta clave para Pregunta Abierta.", e);
            }
        }


        // --------------------------------------------------------
        // 1. Lógica para TRUE_FALSE
        // --------------------------------------------------------
        if ("TRUE_FALSE".equals(questionType)) {
            String correctValueText;

            try {
                // Intenta leer el JSON como un objeto Map (formato esperado: {"key": "V", "value": "TRUE"})
                Map<String, String> correctMap = objectMapper.readValue(
                        rawCorrectAnswer,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
                );
                correctValueText = correctMap.get("value");

            } catch (Exception e) {
                // Si falla la deserialización a Map, intentamos leer como un Booleano o un String simple.
                try {
                    // Opción 1: Intenta leer como un Booleano
                    Boolean boolValue = objectMapper.readValue(rawCorrectAnswer, Boolean.class);
                    correctValueText = boolValue.toString().toUpperCase(); // Convertir a "TRUE" o "FALSE"
                } catch (Exception ex) {
                    // Opción 2: Intentamos asumir que el valor es el String completo
                    correctValueText = rawCorrectAnswer.replace("\"", "").toUpperCase();
                }

                // Validar el resultado después de todos los intentos
                if (!"TRUE".equals(correctValueText) && !"FALSE".equals(correctValueText)) {
                    System.err.println("Error FATAL al procesar pregunta TRUE_FALSE: Valor de respuesta incorrecto después de la corrección.");
                    throw new RuntimeException("Error: Respuesta de Verdadero/Falso no válida (" + rawCorrectAnswer + ").");
                }
            }

            // Asumiendo que las dos opciones de V/F son siempre "TRUE" y "FALSE"
            List<String> tfOptions = Arrays.asList("TRUE", "FALSE");

            for (String optionText : tfOptions) {
                Answer answer = new Answer();
                answer.setText(optionText);
                // NO SE ESTABLECE answer.setQuestion(question) aquí.

                boolean isCorrect = optionText.equalsIgnoreCase(correctValueText);
                answer.setIsCorrect(isCorrect);
                answer.setPointsValue(isCorrect ? question.getPoints() : 0);

                answers.add(answer);
            }
            return answers;
        }

        // --------------------------------------------------------
        // 2. Lógica para MULTIPLE_CHOICE
        // --------------------------------------------------------
        if ("MULTIPLE_CHOICE".equals(questionType)) {

            // Si es MULTIPLE_CHOICE y no tiene opciones, es un error de datos.
            if (question.getOptions() == null || question.getOptions().isEmpty()) {
                System.err.println("Error: El campo 'options' no puede ser nulo para pregunta tipo " + questionType);
                throw new RuntimeException("Error en el formato: El campo 'options' está vacío o nulo.");
            }

            try {
                // Parsear 'options' (Map<clave: letra, valor: texto de la opción>)
                Map<String, String> optionsMap = objectMapper.readValue(
                        question.getOptions(),
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
                );

                // Parsear 'correctAnswer' (ej. {"key": "C", "value": "@RestController"})
                Map<String, String> correctMap = objectMapper.readValue(
                        rawCorrectAnswer,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
                );
                String correctValue = correctMap.get("value");

                // Iterar sobre las opciones para crear entidades Answer
                for (String optionText : optionsMap.values()) {
                    Answer answer = new Answer();
                    answer.setText(optionText);
                    // NO SE ESTABLECE answer.setQuestion(question) aquí.

                    boolean isCorrect = optionText.equals(correctValue);
                    answer.setIsCorrect(isCorrect);
                    answer.setPointsValue(isCorrect ? question.getPoints() : 0);

                    answers.add(answer);
                }

            } catch (Exception e) {
                System.err.println("Error FATAL al parsear JSON de opciones para la pregunta: " + e.getMessage());
                throw new RuntimeException("Error en el formato JSON de opciones o respuesta de la pregunta.", e);
            }

            return answers;
        }

        // Para cualquier otro tipo de pregunta no implementado (ej: ESSAY), devolvemos un set vacío.
        return answers;
    }


    // Método auxiliar para mapear una entidad Question a un DTO QuestionResponse
    private QuestionResponse mapToQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setQuestionText(question.getQuestionText());
        response.setQuestionType(question.getQuestionType());

        response.setDifficulty(question.getDifficulty());
        response.setPoints(question.getPoints());
        response.setOptions(question.getOptions());
        response.setCorrectAnswer(question.getCorrectAnswer());
        response.setExplanation(question.getExplanation());

        if (question.getExam() != null) {
            response.setExamId(question.getExam().getId());
        } else {
            response.setExamId(null);
        }
        return response;
    }
}