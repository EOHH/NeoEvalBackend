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

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
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
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(this::mapToUserResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return mapToUserResponse(user);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", id));
        if (!"STUDENT".equalsIgnoreCase(student.getUserType())) {
            throw new ResourceNotFoundException("Estudiante", "id", id);
        }
        return mapToStudentResponse(student);
    }

    @Override
    public ParentResponse getParentById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", id));
        if (!"PARENT".equalsIgnoreCase(parent.getUserType())) {
            throw new ResourceNotFoundException("Padre", "id", id);
        }
        return mapToParentResponse(parent);
    }

    @Override
    public Page<StudentResponse> getAllStudents(Pageable pageable) {
        Page<Student> studentsPage = studentRepository.findByActiveTrueAndApprovalStatus("APPROVED", pageable);
        return studentsPage.map(this::mapToStudentResponse);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest updateRequest) {
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
                System.out.println("Los campos específicos de TEACHER en UpdateUserRequest deben ser manejados por TeacherService.");
                break;
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponse toggleUserStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        if (user.isActive() == active) {
            return mapToUserResponse(user);
        }

        user.setActive(active);
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // ✅ NUEVOS MÉTODOS PARA APROBACIÓN
    @Override
    public Page<UserResponse> getPendingUsers(Pageable pageable) {
        Page<User> pendingUsersPage = userRepository.findByApprovalStatus("PENDING", pageable);
        return pendingUsersPage.map(this::mapToUserResponse);
    }

    @Override
    @Transactional
    public UserResponse approveUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        if ("APPROVED".equals(user.getApprovalStatus())) {
            throw new IllegalStateException("El usuario ya está aprobado");
        }

        user.setApprovalStatus("APPROVED");
        user.setApprovedBy(adminId);
        user.setApprovedAt(Instant.now());
        user.setRejectionReason(null);

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse rejectUser(Long userId, Long adminId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        if ("REJECTED".equals(user.getApprovalStatus())) {
            throw new IllegalStateException("El usuario ya está rechazado");
        }

        user.setApprovalStatus("REJECTED");
        user.setApprovedBy(adminId);
        user.setApprovedAt(Instant.now());
        user.setRejectionReason(reason);
        user.setActive(false);

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // --- MÉTODOS DE MAPEO ---
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse(user);

        // Si es PARENT, agregar el studentId
        if ("PARENT".equals(user.getUserType())) {
            if (user instanceof Parent) {
                Parent parent = (Parent) user;
                if (parent.getStudent() != null) {
                    response.setStudentId(parent.getStudent().getId());
                }
            } else {
                Parent parent = parentRepository.findById(user.getId()).orElse(null);
                if (parent != null && parent.getStudent() != null) {
                    response.setStudentId(parent.getStudent().getId());
                }
            }
        }

        return response;
    }

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse response = new StudentResponse(student); // ✅ Constructor con User
        response.setEducationalLevel(student.getEducationalLevel());
        response.setBirthDate(student.getBirthDate());
        // Agregar más campos si existen
        return response;
    }

    private ParentResponse mapToParentResponse(Parent parent) {
        ParentResponse response = new ParentResponse(parent); // ✅ Constructor con User
        response.setRelationship(parent.getRelationship());

        if (parent.getStudent() != null) {
            response.setStudentId(parent.getStudent().getId());
            response.setStudentName(parent.getStudent().getName());
            response.setStudentEducationalLevel(parent.getStudent().getEducationalLevel());
        }

        return response;
    }
}
