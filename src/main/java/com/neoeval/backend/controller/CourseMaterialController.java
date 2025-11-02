package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.ClassSessionRequest;
import com.neoeval.backend.dto.request.CourseModuleRequest;
import com.neoeval.backend.dto.request.MaterialResourceRequest;
import com.neoeval.backend.dto.response.ClassSessionResponse;
import com.neoeval.backend.dto.response.CourseModuleResponse;
import com.neoeval.backend.dto.response.MaterialResourceResponse;
import com.neoeval.backend.security.UserPrincipal;
import com.neoeval.backend.service.CourseMaterialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/materials") // Base: /api/materials
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;

    public CourseMaterialController(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
    }

    // ======================================================
    // 🔐 Helper para obtener el ID del usuario autenticado (asumiendo que es Teacher)
    // ======================================================
    private Long getCurrentTeacherId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            // Se asume que el ID del UserPrincipal es el ID del Teacher si el rol es TEACHER
            return userPrincipal.getId();
        }
        throw new IllegalStateException("El principal no es una instancia de UserPrincipal");
    }

    // ===================================
    // 1️⃣ COURSE MODULE (CRUD)
    // ===================================

    /** POST /api/materials/modules - Crea un nuevo módulo */
    @PostMapping("/modules")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseModuleResponse> createModule(
            @Valid @RequestBody CourseModuleRequest request,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        CourseModuleResponse response = courseMaterialService.createModule(request, teacherId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /** GET /api/materials/modules/teacher/{teacherId} - Obtiene todos los módulos de un profesor */
    @GetMapping("/modules/teacher/{teacherId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseModuleResponse>> getModulesByTeacher(@PathVariable Long teacherId) {
        List<CourseModuleResponse> response = courseMaterialService.getModulesByTeacher(teacherId);
        return ResponseEntity.ok(response);
    }

    // ✅ NUEVO ENDPOINT PARA ESTUDIANTES
    /** GET /api/materials/modules/student/{studentId} - Obtiene módulos por ID de estudiante */
    @GetMapping("/modules/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseModuleResponse>> getModulesForStudent(@PathVariable Long studentId) {

        // ⚠️ NOTA DE SEGURIDAD: En un entorno de producción, es una mala práctica
        // exponer el ID como PathVariable. Deberías obtener el ID del estudiante
        // desde el token de autenticación (Authentication principal) para
        // asegurar que solo pueda acceder a sus propios módulos.

        List<CourseModuleResponse> modules = courseMaterialService.getModulesForStudent(studentId);

        return ResponseEntity.ok(modules);
    }

    /** GET /api/materials/modules/{moduleId} - Obtiene un módulo con sus sesiones y recursos */
    @GetMapping("/modules/{moduleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CourseModuleResponse> getModuleWithDetails(@PathVariable Long moduleId) {
        CourseModuleResponse response = courseMaterialService.getModuleWithDetails(moduleId);
        return ResponseEntity.ok(response);
    }

    /** PUT /api/materials/modules/{moduleId} - Actualiza un módulo */
    @PutMapping("/modules/{moduleId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseModuleResponse> updateModule(
            @PathVariable Long moduleId,
            @Valid @RequestBody CourseModuleRequest request,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        CourseModuleResponse response = courseMaterialService.updateModule(moduleId, request, teacherId);
        return ResponseEntity.ok(response);
    }

    /** DELETE /api/materials/modules/{moduleId} - Elimina un módulo */
    @DeleteMapping("/modules/{moduleId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteModule(
            @PathVariable Long moduleId,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        courseMaterialService.deleteModule(moduleId, teacherId);
        return ResponseEntity.noContent().build();
    }

    // ===================================
    // 2️⃣ CLASS SESSION (CRUD)
    // ===================================

    /** POST /api/materials/modules/{moduleId}/sessions - Crea una nueva sesión */
    @PostMapping("/modules/{moduleId}/sessions")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ClassSessionResponse> createSession(
            @PathVariable Long moduleId,
            @Valid @RequestBody ClassSessionRequest request,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        ClassSessionResponse response = courseMaterialService.createSession(moduleId, request, teacherId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /** GET /api/materials/sessions/{sessionId} - Obtiene una sesión y sus recursos */
    @GetMapping("/sessions/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClassSessionResponse> getSession(@PathVariable Long sessionId) {
        ClassSessionResponse response = courseMaterialService.getSession(sessionId);
        return ResponseEntity.ok(response);
    }

    /** PUT /api/materials/modules/{moduleId}/sessions/{sessionId} - Actualiza una sesión */
    @PutMapping("/modules/{moduleId}/sessions/{sessionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ClassSessionResponse> updateSession(
            @PathVariable Long moduleId,
            @PathVariable Long sessionId,
            @Valid @RequestBody ClassSessionRequest request,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        ClassSessionResponse response = courseMaterialService.updateSession(moduleId, sessionId, request, teacherId);
        return ResponseEntity.ok(response);
    }

    /** DELETE /api/materials/modules/{moduleId}/sessions/{sessionId} - Elimina una sesión */
    @DeleteMapping("/modules/{moduleId}/sessions/{sessionId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long moduleId,
            @PathVariable Long sessionId,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        courseMaterialService.deleteSession(moduleId, sessionId, teacherId);
        return ResponseEntity.noContent().build();
    }

    // ===================================
    // 3️⃣ MATERIAL RESOURCE (CRUD)
    // ===================================

    /** * POST /api/materials/sessions/{sessionId}/resources - Agrega un recurso.
     * Soporta 'multipart/form-data' para subir archivos.
     */
    @PostMapping(value = "/sessions/{sessionId}/resources", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResourceResponse> createResource(
            @PathVariable Long sessionId,
            // 🎯 CAMBIO CLAVE: Leer el DTO como String
            @RequestParam("request") String requestJson,
            // El archivo sigue siendo @RequestParam
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication) {

        MaterialResourceRequest request;
        try {
            // Deserialización manual (usando Jackson que ya tienes)
            ObjectMapper objectMapper = new ObjectMapper();
            request = objectMapper.readValue(requestJson, MaterialResourceRequest.class);

            // Si el DTO necesita validación, puedes usar @Valid aquí si lo implementaste
            // o realizar validación manual.

        } catch (IOException e) {
            // Error si el JSON no es válido
            System.err.println("Error al deserializar JSON multipart: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Long teacherId = getCurrentTeacherId(authentication);

        MaterialResourceResponse response = courseMaterialService.createResource(sessionId, request, teacherId, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /** PUT /api/materials/sessions/{sessionId}/resources/{resourceId} - Actualiza un recurso */
    @PutMapping("/sessions/{sessionId}/resources/{resourceId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<MaterialResourceResponse> updateResource(
            @PathVariable Long sessionId,
            @PathVariable Long resourceId,
            @Valid @RequestBody MaterialResourceRequest request,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        MaterialResourceResponse response = courseMaterialService.updateResource(sessionId, resourceId, request, teacherId);
        return ResponseEntity.ok(response);
    }

    /** GET /api/materials/download - Sirve un archivo para su descarga o visualización en el navegador */
    @GetMapping("/download")
    @PreAuthorize("isAuthenticated()") // Acceso para cualquier usuario autenticado
    public ResponseEntity<byte[]> downloadFile(@RequestParam("path") String storagePath) {
        return courseMaterialService.downloadFile(storagePath);
    }


    /** DELETE /api/materials/sessions/{sessionId}/resources/{resourceId} - Elimina un recurso */
    @DeleteMapping("/sessions/{sessionId}/resources/{resourceId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long sessionId,
            @PathVariable Long resourceId,
            Authentication authentication) {

        Long teacherId = getCurrentTeacherId(authentication);
        courseMaterialService.deleteResource(sessionId, resourceId, teacherId);
        return ResponseEntity.noContent().build();
    }
}