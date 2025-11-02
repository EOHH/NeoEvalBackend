package com.neoeval.backend.service.impl;

import com.neoeval.backend.entity.StudentResult;
import com.neoeval.backend.repository.StudentResultRepository;
import com.neoeval.backend.service.ProgressService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final StudentResultRepository studentResultRepository;

    public ProgressServiceImpl(StudentResultRepository studentResultRepository) {
        this.studentResultRepository = studentResultRepository;
    }

    @Override
    public Map<String, Object> getStudentProgressMetrics(Long studentId) {
        // 1. Obtener resultados del estudiante actual
        // Asume que el repositorio devuelve una lista vacía, no null.
        List<StudentResult> results = studentResultRepository.findByStudentId(studentId);

        Map<String, Object> metrics = new HashMap<>();

        // 1.1. MANEJO DE LISTA VACÍA
        if (results == null || results.isEmpty()) {
            metrics.put("averageScore", 0.0);
            // El ranking es 0 (No aplica) si el estudiante no tiene resultados
            metrics.put("rankingPosition", 0);
            metrics.put("progressTrend", 0.0);
            metrics.put("completedQuizzes", 0);
            return metrics;
        }

        // 2. CÁLCULO DE MÉTRICAS BASE
        double averageScore = results.stream()
                .mapToDouble(StudentResult::getPercentage)
                .average()
                .orElse(0.0);

        int completedQuizzes = results.size();

        // Cálculo protegido de la tendencia
        double progressTrend = calculateProgressTrend(results);

        // 🚀 CÁLCULO DINÁMICO DEL RANKING (Obtenido de la DB)
        int rankingPosition = calculateRankingPosition(studentId);

        // 3. RETORNO DE MÉTRICAS (redondeadas)
        metrics.put("averageScore", Math.round(averageScore * 10.0) / 10.0);
        metrics.put("rankingPosition", rankingPosition);
        metrics.put("progressTrend", Math.round(progressTrend * 10.0) / 10.0);
        metrics.put("completedQuizzes", completedQuizzes);

        return metrics;
    }

    /**
     * Método central para calcular la posición de ranking de forma dinámica.
     * Utiliza el método findAllStudentAverages() del repositorio para obtener
     * el promedio de todos los estudiantes, ordenar la lista y encontrar la posición.
     * @param studentId ID del estudiante a rankear.
     * @return Posición del ranking (1, 2, 3...) o 0 si no se encuentra.
     */
    private int calculateRankingPosition(Long studentId) {
        // Llama al método que usa la consulta JPQL de la DB.
        // Devuelve List<[ID_Estudiante (Long), Promedio_Score (Double)]>
        List<Object[]> allAverages = studentResultRepository.findAllStudentAverages();

        // 1. Convertir los Object[] en una lista de Map.Entry para facilitar la manipulación
        List<Map.Entry<Long, Double>> rankingEntries = allAverages.stream()
                .map(arr -> {
                    // Se asume que arr[0] es Long (studentId) y arr[1] es Double (AVG)
                    Long id = (Long) arr[0];
                    Double avg = (Double) arr[1];
                    return Map.entry(id, avg);
                })
                .collect(Collectors.toList());

        // 2. Ordenar la lista por promedio de forma descendente (el puntaje más alto es el #1)
        List<Map.Entry<Long, Double>> sortedRankings = rankingEntries.stream()
                // Comparator.reverseOrder() asegura orden descendente
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // 3. Encontrar la posición del estudiante actual en la lista ordenada
        for (int i = 0; i < sortedRankings.size(); i++) {
            Long currentId = sortedRankings.get(i).getKey();
            if (currentId.equals(studentId)) {
                // La posición del ranking es el índice + 1
                return i + 1;
            }
        }

        // Si el estudiante no está en la lista (ej. no tiene resultados), devuelve 0
        return 0;
    }

    /**
     * Método auxiliar para calcular la tendencia de progreso.
     * Compara el promedio de los últimos 5 resultados con los 5 anteriores.
     */
    private double calculateProgressTrend(List<StudentResult> results) {
        final int requiredResults = 10;

        // Si no hay suficientes datos para comparar dos grupos de 5.
        if (results.size() < requiredResults) {
            return 0.0;
        }

        // 5 resultados ANTERIORES (Indices: size - 10 hasta size - 5)
        List<StudentResult> previousFive = results.subList(results.size() - 10, results.size() - 5);

        // ÚLTIMOS 5 resultados (Indices: size - 5 hasta size)
        List<StudentResult> lastFive = results.subList(results.size() - 5, results.size());

        // Cálculo de promedios
        double avgLast = lastFive.stream().mapToDouble(StudentResult::getPercentage).average().orElse(0.0);
        double avgPrev = previousFive.stream().mapToDouble(StudentResult::getPercentage).average().orElse(0.0);

        // La diferencia es la tendencia: positivo = mejora, negativo = empeora.
        return avgLast - avgPrev;
    }
}
