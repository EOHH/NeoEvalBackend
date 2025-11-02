package com.neoeval.backend.controller;

import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.UserResponse;
import com.neoeval.backend.dto.response.StudentResponse;
import com.neoeval.backend.dto.response.ParentResponse;
import com.neoeval.backend.security.UserPrincipal;
import com.neoeval.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Ajusta según tus necesidades de CORS
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // ✅ Obtener estudiante por ID
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        StudentResponse student = userService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // ✅ Obtener padre/madre por ID
    @GetMapping("/parents/{id}")
    public ResponseEntity<ParentResponse> getParentById(@PathVariable Long id) {
        ParentResponse parent = userService.getParentById(id);
        return ResponseEntity.ok(parent);
    }

    // ✅ Obtener todos los estudiantes
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // ✅ Actualizar usuario (ENDPOINT CRÍTICO PARA EL PERFIL)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateRequest) {
        UserResponse updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // ✅ Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Endpoint adicional: Obtener perfil del usuario actual
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // Aquí obtienes el ID del usuario desde el principal
        Long userId = userPrincipal.getId();

        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> toggleUserStatus(
            @PathVariable Long id,
            // NOTA: Usamos @RequestParam para pasar el nuevo estado 'active=true/false'
            @RequestParam("active") boolean active) {

        UserResponse updatedUser = userService.toggleUserStatus(id, active);
        return ResponseEntity.ok(updatedUser);
    }

    // ✅ Endpoint adicional: Actualizar perfil del usuario actual
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateCurrentUserProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateUserRequest updateRequest) {
        Long userId = userPrincipal.getId();

        UserResponse updatedUser = userService.updateUser(userId, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}