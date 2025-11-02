package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.UserRequest; // Para Crear/Actualizar
import com.neoeval.backend.dto.request.PointsUpdateRequest; // Para Sumar Puntos
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // --- 1. MÉTODOS CRUD ---

    // Crear un nuevo estudiante (POST)
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @Valid @RequestBody UserRequest request) {
        // Asumimos que el servicio valida userType='STUDENT'
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Actualizar la información de un estudiante (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        StudentResponse response = studentService.updateStudent(id, request);
        return ResponseEntity.ok(response);
    }

    // Eliminar un estudiante (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }


    // --- 2. MÉTODOS DE LECTURA (Lo que ya tenías) ---

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        StudentResponse response = studentService.getStudentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> responses = studentService.getAllStudents();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudentsByName(@RequestParam("name") String name) {
        List<StudentResponse> responses = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<StudentResponse>> getStudentsByGroup(@PathVariable Long groupId) {
        List<StudentResponse> responses = studentService.getStudentsByGroup(groupId);
        return ResponseEntity.ok(responses);
    }

    // --- 3. MÉTODO DE GAMIFICACIÓN CLAVE ---

    /**
     * Endpoint para sumar puntos a un estudiante y disparar la asignación de logros.
     * Ejemplo de uso: PATCH /api/students/1/add-points
     */
    @PatchMapping("/{id}/add-points")
    public ResponseEntity<StudentResponse> addPointsToStudent(
            @PathVariable Long id,
            @Valid @RequestBody PointsUpdateRequest request) {

        StudentResponse response = studentService.addPointsToStudent(id, request.getPointsToAdd());
        return ResponseEntity.ok(response);
    }
}