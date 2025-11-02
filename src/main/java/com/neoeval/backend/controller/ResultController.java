package com.neoeval.backend.controller;

import com.neoeval.backend.dto.response.QuizReviewResponse;
import com.neoeval.backend.service.ResultReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Nueva ruta base más simple para la revisión detallada
@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultReviewService reviewService;

    public ResultController(ResultReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Endpoint para obtener los detalles completos de la revisión de un quiz.
     * URL: GET /api/results/{resultId}
     */
    @GetMapping("/{resultId}")
    public ResponseEntity<QuizReviewResponse> getQuizReview(@PathVariable Long resultId) {
        QuizReviewResponse review = reviewService.getReviewDetails(resultId);
        return ResponseEntity.ok(review);
    }
}