package com.neoeval.backend.service;

import java.util.Map;

public interface ProgressService {

    // Retorna un mapa de métricas clave para la pantalla de Progreso del frontend
    Map<String, Object> getStudentProgressMetrics(Long studentId);
}