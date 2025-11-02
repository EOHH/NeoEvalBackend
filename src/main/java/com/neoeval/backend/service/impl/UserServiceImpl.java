// Archivo: com/neoeval/backend/service/impl/UserServiceImpl.java
package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.*;
import com.neoeval.backend.entity.*;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.*;
import com.neoeval.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        // Esta llamada ahora devolverá un UserResponse completo
        return mapToUserResponse(user);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        // ... (sin cambios) ...
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        if (!"STUDENT".equalsIgnoreCase(student.getUserType())) {
            throw new ResourceNotFoundException("Estudiante", "id", id);
        }
        return mapToStudentResponse(student);
    }

    @Override
    public ParentResponse getParentById(Long id) {
        // ... (sin cambios) ...
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", id));
        if (!"PARENT".equalsIgnoreCase(parent.getUserType())) {
            throw new ResourceNotFoundException("Padre", "id", id);
        }
        return mapToParentResponse(parent);
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        // ... (sin cambios) ...
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToStudentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest updateRequest) {
        // ... (sin cambios) ...
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (updateRequest.getName() != null) user.setName(updateRequest.getName());
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("El correo ya está en uso");
            }
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        switch (user.getUserType()) {
            case "STUDENT":
                if (user instanceof Student) {
                    Student student = (Student) user;
                    if (updateRequest.getEducationalLevel() != null) student.setEducationalLevel(updateRequest.getEducationalLevel());
                    if (updateRequest.getBirthDate() != null) student.setBirthDate(updateRequest.getBirthDate());
                    studentRepository.save(student);
                } else {
                    throw new IllegalStateException("El usuario con ID " + id + " es de tipo ESTUDIANTE pero no es una instancia de Student.");
                }
                break;
            case "PARENT":
                if (user instanceof Parent) {
                    Parent parent = (Parent) user;
                    if (updateRequest.getRelationship() != null) parent.setRelationship(updateRequest.getRelationship());
                    parentRepository.save(parent);
                } else {
                    throw new IllegalStateException("El usuario con ID " + id + " es de tipo PADRE pero no es una instancia de Parent.");
                }
                break;
            case "TEACHER":
                System.out.println("Los campos específicos de TEACHER en UpdateUserRequest (department, subjectTaught) deben ser manejados por TeacherService, si se necesita su persistencia en una entidad Teacher.");
                break;
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // ... (sin cambios) ...
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponse toggleUserStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // 1. Verificar si hay un cambio de estado necesario
        if (user.isActive() == active) {
            // Si el estado ya es el deseado, no hacemos nada y devolvemos el usuario
            return mapToUserResponse(user);
        }

        // 2. Aplicar el cambio de estado
        user.setActive(active);

        // 3. Guardar y devolver la respuesta
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // --- Métodos de Mapeo (Helper Methods) ---

    // ✅✅✅ CORRECCIÓN PRINCIPAL AQUÍ ✅✅✅
    private UserResponse mapToUserResponse(User user) {
        // 1. Crea la respuesta base como antes
        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType(),
                user.getCreatedAt(), // Asumiendo que estos son Instant o Date
                user.getLastLogin(),  // Asumiendo que estos son Instant o Date
                user.isActive()
        );

        // 2. Lógica para enriquecer la respuesta
        if ("PARENT".equals(user.getUserType())) {
            // Hacemos un cast seguro. (userRepository.findById devuelve un User,
            // pero si es PARENT, debe ser una instancia de Parent)
            if (user instanceof Parent) {
                Parent parent = (Parent) user;
                if (parent.getStudent() != null) {
                    // 3. Establece el studentId en el DTO
                    response.setStudentId(parent.getStudent().getId());
                }
            } else {
                // Si esto falla, puede que necesites cargar el Parent explícitamente
                Parent parent = parentRepository.findById(user.getId())
                        .orElse(null); // Opcional: Cargar si el 'user' no es la instancia correcta
                if (parent != null && parent.getStudent() != null) {
                    response.setStudentId(parent.getStudent().getId());
                }
            }
        }

        // 4. Retorna la respuesta (ahora con el studentId si aplica)
        return response;
    }

    private StudentResponse mapToStudentResponse(Student student) {
        // ... (sin cambios) ...
        StudentResponse response = new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getUserType(),
                student.getCreatedAt(),
                student.getLastLogin(),
                student.isActive(),
                student.getEducationalLevel(),
                student.getBirthDate(),
                null, null, null, null, null, null
        );
        return response;
    }

    private ParentResponse mapToParentResponse(Parent parent) {
        // ... (sin cambios) ...
        // ✅ NOTA: Este método SÍ debe incluir el studentId
        ParentResponse response = new ParentResponse(
                parent.getId(),
                parent.getName(),
                parent.getEmail(),
                parent.getUserType(),
                parent.getCreatedAt(),
                parent.getLastLogin(),
                parent.isActive(),
                parent.getRelationship(),
                null, null, null
        );

        return response;
    }
}