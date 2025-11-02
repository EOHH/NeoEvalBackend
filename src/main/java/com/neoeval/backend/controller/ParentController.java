package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.RegisterRequest; // Para crear un Parent
import com.neoeval.backend.dto.request.UpdateUserRequest; // Para actualizar un Parent
import com.neoeval.backend.dto.response.ParentResponse;
import com.neoeval.backend.service.ParentService;
import jakarta.validation.Valid; // Para la validación de los DTOs de request
import org.springframework.http.HttpStatus; // Para los códigos de estado HTTP
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/parents") // Define la ruta base para todos los endpoints en este controlador
public class ParentController {

    private final ParentService parentService; // Inyecta el servicio de Parent

    // Constructor para inyección de dependencias
    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    /**
     * Endpoint para registrar un nuevo padre.
     * @param registerRequest DTO con los datos de registro del padre.
     * @return ResponseEntity con el ParentResponse del padre creado y estado HTTP 201.
     */
    @PostMapping // Mapea las solicitudes POST a /api/parents
    public ResponseEntity<ParentResponse> createParent(@Valid @RequestBody RegisterRequest registerRequest) {
        ParentResponse response = parentService.createParent(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Retorna 201 Created
    }

    /**
     * Endpoint para obtener un padre por su ID.
     * @param id ID del padre a buscar.
     * @return ResponseEntity con el ParentResponse del padre encontrado y estado HTTP 200.
     */
    @GetMapping("/{id}") // Mapea las solicitudes GET a /api/parents/{id}
    public ResponseEntity<ParentResponse> getParentById(@PathVariable Long id) {
        ParentResponse response = parentService.getParentById(id);
        return ResponseEntity.ok(response); // Retorna 200 OK
    }

    /**
     * Endpoint para obtener todos los padres.
     * @return ResponseEntity con una lista de ParentResponse y estado HTTP 200.
     */
    @GetMapping // Mapea las solicitudes GET a /api/parents
    public ResponseEntity<List<ParentResponse>> getAllParents() {
        List<ParentResponse> responses = parentService.getAllParents();
        return ResponseEntity.ok(responses); // Retorna 200 OK
    }

    /**
     * Endpoint para actualizar un padre existente.
     * @param id ID del padre a actualizar.
     * @param updateRequest DTO con los datos para actualizar el padre.
     * @return ResponseEntity con el ParentResponse del padre actualizado y estado HTTP 200.
     */
    @PutMapping("/{id}") // Mapea las solicitudes PUT a /api/parents/{id}
    public ResponseEntity<ParentResponse> updateParent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateRequest) {
        ParentResponse response = parentService.updateParent(id, updateRequest);
        return ResponseEntity.ok(response); // Retorna 200 OK
    }

    /**
     * Endpoint para eliminar un padre por su ID.
     * @param id ID del padre a eliminar.
     * @return ResponseEntity sin contenido y estado HTTP 204.
     */
    @DeleteMapping("/{id}") // Mapea las solicitudes DELETE a /api/parents/{id}
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 No Content
    }

    /**
     * Endpoint para vincular un estudiante a un padre.
     * @param parentId ID del padre.
     * @param studentId ID del estudiante a vincular.
     * @return ResponseEntity con el ParentResponse del padre actualizado y estado HTTP 200.
     */
    @PostMapping("/{parentId}/link-student/{studentId}") // Mapea las solicitudes POST a /api/parents/{parentId}/link-student/{studentId}
    public ResponseEntity<ParentResponse> linkStudentToParent(
            @PathVariable Long parentId,
            @PathVariable Long studentId) {
        ParentResponse response = parentService.linkStudentToParent(parentId, studentId);
        return ResponseEntity.ok(response); // Retorna 200 OK
    }

    // Nota: El método getParentByStudentId que tenías previamente no está en ParentService.
    // Si necesitas este método, debería estar en StudentService (para obtener el padre de un estudiante)
    // o deberías añadirlo a ParentService y a ParentRepository si la relación lo permite (e.g. findByStudentId).
    // Por ahora, lo he comentado para evitar errores de compilación si no está implementado en el servicio.
    /*
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ParentResponse> getParentByStudentId(@PathVariable Long studentId) {
        // Asumiendo que ParentService tiene este método o que se obtiene del StudentService
        // Si ParentService tiene un método 'findByStudentId', úsalo:
        // ParentResponse response = parentService.getParentByStudentId(studentId);
        // return ResponseEntity.ok(response);

        // Si necesitas obtener al padre a través del estudiante, esto iría en StudentService
        // StudentResponse student = studentService.getStudentById(studentId);
        // if (student.getParentId() != null) {
        //     return ResponseEntity.ok(parentService.getParentById(student.getParentId()));
        // }
        throw new ResourceNotFoundException("Padre", "estudianteId", studentId);
    }
    */
}