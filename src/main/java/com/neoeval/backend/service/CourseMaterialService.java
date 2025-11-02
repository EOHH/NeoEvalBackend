package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.ClassSessionRequest;
import com.neoeval.backend.dto.request.CourseModuleRequest;
import com.neoeval.backend.dto.request.MaterialResourceRequest;
import com.neoeval.backend.dto.response.ClassSessionResponse;
import com.neoeval.backend.dto.response.CourseModuleResponse;
import com.neoeval.backend.dto.response.MaterialResourceResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile; // 👈 NUEVA IMPORTACIÓN

public interface CourseMaterialService {

    // --- Operaciones de CourseModule ---

    // ... (Métodos de CourseModule sin cambios)

    CourseModuleResponse createModule(CourseModuleRequest request, Long currentTeacherId);
    CourseModuleResponse updateModule(Long moduleId, CourseModuleRequest request, Long currentTeacherId);
    CourseModuleResponse getModuleWithDetails(Long moduleId);
    List<CourseModuleResponse> getModulesByTeacher(Long teacherId);
    void deleteModule(Long moduleId, Long currentTeacherId);

    // --- Operaciones de ClassSession ---

    // ... (Métodos de ClassSession sin cambios)

    ClassSessionResponse createSession(Long moduleId, ClassSessionRequest request, Long currentTeacherId);
    ClassSessionResponse updateSession(Long moduleId, Long sessionId, ClassSessionRequest request, Long currentTeacherId);
    ClassSessionResponse getSession(Long sessionId);
    void deleteSession(Long moduleId, Long sessionId, Long currentTeacherId);

    // --- Operaciones de MaterialResource ---

    /**
     * Agrega un nuevo recurso (archivo/enlace) a una sesión.
     * @param file El archivo a subir (puede ser null si es solo un enlace).
     */
    MaterialResourceResponse createResource(Long sessionId, MaterialResourceRequest request, Long currentTeacherId, MultipartFile file); // 👈 MÉTODO ACTUALIZADO

    /**
     * Actualiza la información de un recurso.
     */
    MaterialResourceResponse updateResource(Long sessionId, Long resourceId, MaterialResourceRequest request, Long currentTeacherId);

    ResponseEntity<byte[]> downloadFile(String storagePath);

    /**
     * ✅ NUEVO: Obtiene todos los módulos de curso a los que un estudiante
     * tiene acceso (basado en sus grupos de clase y asignaturas).
     */
    List<CourseModuleResponse> getModulesForStudent(Long studentId);

    /**
     * Elimina un recurso (y el archivo asociado, si existe).
     */
    void deleteResource(Long sessionId, Long resourceId, Long currentTeacherId);
}