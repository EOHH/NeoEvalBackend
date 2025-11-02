package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.AchievementRequest;
import com.neoeval.backend.dto.response.AchievementResponse;
import com.neoeval.backend.service.AchievementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // 1. Crear un nuevo logro (POST)
    @PostMapping
    public ResponseEntity<AchievementResponse> createAchievement(
            @Valid @RequestBody AchievementRequest request) {
        AchievementResponse response = achievementService.createAchievement(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Obtener un logro por ID (GET /api/achievements/{id})
    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponse> getAchievementById(@PathVariable Long id) {
        AchievementResponse response = achievementService.getAchievementById(id);
        return ResponseEntity.ok(response);
    }

    // 3. Obtener todos los logros (GET /api/achievements)
    @GetMapping
    public ResponseEntity<List<AchievementResponse>> getAllAchievements() {
        List<AchievementResponse> responseList = achievementService.getAllAchievements();
        return ResponseEntity.ok(responseList);
    }

    // 4. Actualizar un logro por ID (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponse> updateAchievement(
            @PathVariable Long id,
            @Valid @RequestBody AchievementRequest request) {
        AchievementResponse response = achievementService.updateAchievement(id, request);
        return ResponseEntity.ok(response);
    }

    // 5. Eliminar un logro por ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Consulta por puntos (GET /api/achievements/search?points=100)
    @GetMapping("/search")
    public ResponseEntity<List<AchievementResponse>> getAchievementsByPoints(
            @RequestParam(name = "points") Integer points) {

        List<AchievementResponse> responseList =
                achievementService.getAchievementsByPointsLessOrEqualTo(points);

        return ResponseEntity.ok(responseList);
    }

    // 7. Consulta por nombre (GET /api/achievements/name/ExpertoJava)
    @GetMapping("/name/{name}")
    public ResponseEntity<AchievementResponse> getAchievementByName(@PathVariable String name) {
        AchievementResponse response = achievementService.getAchievementByName(name);
        return ResponseEntity.ok(response);
    }
}