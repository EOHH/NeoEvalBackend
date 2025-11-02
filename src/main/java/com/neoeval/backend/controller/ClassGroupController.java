package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.CreateGroupRequest;
import com.neoeval.backend.dto.request.StudentIdsRequest; // ✅ Importar el DTO de IDs
import com.neoeval.backend.dto.response.ClassGroupResponse;
import com.neoeval.backend.service.ClassGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class ClassGroupController {

    private final ClassGroupService classGroupService;

    public ClassGroupController(ClassGroupService classGroupService) {
        this.classGroupService = classGroupService;
    }

    @PostMapping // POST /api/groups - Crear un nuevo grupo
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ClassGroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest groupRequest) {
        ClassGroupResponse response = classGroupService.createGroup(groupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}") // GET /api/groups/{id} - Obtener un grupo por ID
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT', 'ADMIN')")
    public ResponseEntity<ClassGroupResponse> getGroupById(@PathVariable Long id) {
        ClassGroupResponse response = classGroupService.getGroupById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping // GET /api/groups - Obtener todos los grupos
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<ClassGroupResponse>> getAllGroups() {
        List<ClassGroupResponse> responses = classGroupService.getAllGroups();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/teacher/{teacherId}") // GET /api/groups/teacher/{teacherId} - Obtener grupos por profesor
    @PreAuthorize("hasRole('TEACHER') and #teacherId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<List<ClassGroupResponse>> getGroupsByTeacher(@PathVariable Long teacherId) {
        List<ClassGroupResponse> responses = classGroupService.getGroupsByTeacherId(teacherId);
        return ResponseEntity.ok(responses);
    }

    // ✅ NUEVO ENDPOINT: Agrega múltiples estudiantes (Resuelve el Error 500)
    // POST /api/groups/{groupId}/students
    @PostMapping("/{groupId}/students")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ClassGroupResponse> addStudentsToGroup(
            @PathVariable Long groupId,
            // Mapea el cuerpo JSON {"studentIds": [...]} al DTO
            @Valid @RequestBody StudentIdsRequest request) {

        ClassGroupResponse response = classGroupService.addStudentsToGroup(groupId, request.getStudentIds());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{groupId}/students/{studentId}") // POST /api/groups/{groupId}/students/{studentId} - Añadir estudiante a grupo
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ClassGroupResponse> addStudentToGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId) {
        ClassGroupResponse response = classGroupService.addStudentToGroup(groupId, studentId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}/students/{studentId}") // DELETE /api/groups/{groupId}/students/{studentId} - Remover estudiante de grupo
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ClassGroupResponse> removeStudentFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId) {
        ClassGroupResponse response = classGroupService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}") // PUT /api/groups/{id} - Actualizar un grupo existente
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<ClassGroupResponse> updateGroup(
            @PathVariable Long id,
            @Valid @RequestBody CreateGroupRequest groupRequest) {
        ClassGroupResponse response = classGroupService.updateGroup(id, groupRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}") // DELETE /api/groups/{id} - Eliminar un grupo
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        classGroupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}