package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.QuestionRequest;
import com.neoeval.backend.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(Long examId, QuestionRequest questionRequest);
    QuestionResponse getQuestionById(Long id);
    List<QuestionResponse> getAllQuestions(); // Nuevo método para obtener todas las preguntas
    QuestionResponse updateQuestion(Long id, QuestionRequest questionRequest); // Nuevo método para actualizar
    void deleteQuestion(Long id); // Nuevo método para eliminar
    List<QuestionResponse> getQuestionsByExamId(Long examId); // Nuevo método para obtener preguntas por examen
}