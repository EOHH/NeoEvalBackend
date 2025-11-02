package com.neoeval.backend.service;

import com.neoeval.backend.dto.response.QuizReviewResponse;

public interface ResultReviewService {

    /**
     * Obtiene todos los detalles de una sumisión de quiz para propósitos de revisión.
     * Combina la data del resultado (StudentResult), del examen (Exam) y las respuestas
     * corregidas.
     *
     * @param resultId El ID del resultado (StudentResult) a revisar.
     * @return El DTO completo QuizReviewResponse.
     */
    QuizReviewResponse getReviewDetails(Long resultId);
}