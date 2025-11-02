package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.RegisterRequest;
import com.neoeval.backend.dto.request.UpdateUserRequest;
import com.neoeval.backend.dto.response.ParentResponse;
import com.neoeval.backend.entity.Parent;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.ParentRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.service.ParentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// CAMBIO: Usamos Instant para manejar timestamps (mejor práctica)
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public ParentServiceImpl(ParentRepository parentRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ParentResponse createParent(RegisterRequest registerRequest) {
        if (parentRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        Parent parent = new Parent();
        parent.setName(registerRequest.getName());
        parent.setEmail(registerRequest.getEmail());
        parent.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Asumiendo que RegisterRequest tiene 'relationship'
        // (Tu entidad Parent tiene este campo, así que el request debería tenerlo)
        if (registerRequest.getRelationship() != null) {
            parent.setRelationship(registerRequest.getRelationship());
        }

        // Los campos 'userType', 'createdAt' y 'active'
        // se manejan automáticamente por los constructores de la entidad.
        // No es necesario setearlos aquí.

        Parent savedParent = parentRepository.save(parent);
        return mapToParentResponse(savedParent);
    }

    @Override
    public ParentResponse getParentById(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", id));
        return mapToParentResponse(parent);
    }

    @Override
    public List<ParentResponse> getAllParents() {
        List<Parent> parents = parentRepository.findAll();
        return parents.stream()
                .map(this::mapToParentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParentResponse updateParent(Long id, UpdateUserRequest updateRequest) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", id));

        if (updateRequest.getName() != null) parent.setName(updateRequest.getName());
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(parent.getEmail())) {
            if (parentRepository.existsByEmail(updateRequest.getEmail())) {
                throw new IllegalArgumentException("El correo electrónico ya está en uso.");
            }
            parent.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            parent.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        if (updateRequest.isActive() != null) { // Utiliza isActive() de UpdateUserRequest
            parent.setActive(updateRequest.isActive());
        }

        // Actualizar campos específicos de Parent si UpdateUserRequest los incluye
        if (updateRequest.getRelationship() != null) {
            parent.setRelationship(updateRequest.getRelationship());
        }

        Parent updatedParent = parentRepository.save(parent);
        return mapToParentResponse(updatedParent);
    }

    @Override
    @Transactional
    public void deleteParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", id));
        parentRepository.delete(parent);
    }

    @Override
    @Transactional
    public ParentResponse linkStudentToParent(Long parentId, Long studentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Padre", "id", parentId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        // Asumiendo que setStudent en Parent maneja la bidireccionalidad (student.setParent(this))
        parent.setStudent(student);

        parentRepository.save(parent); // Guardará el parent y, por cascade, el student si hay cambios en la relación

        return mapToParentResponse(parent);
    }

    private ParentResponse mapToParentResponse(Parent parent) {
        // Uso de setters para mayor claridad y flexibilidad al inicializar
        ParentResponse response = new ParentResponse();
        response.setId(parent.getId());
        response.setName(parent.getName());
        response.setEmail(parent.getEmail());
        response.setUserType(parent.getUserType());

        // Estos métodos ahora manejan Instant, compatible con ParentResponse.
        response.setCreatedAt(parent.getCreatedAt());
        response.setLastLogin(parent.getLastLogin());

        response.setActive(parent.isActive());
        response.setRelationship(parent.getRelationship());

        if (parent.getStudent() != null) {
            response.setStudentId(parent.getStudent().getId());
            response.setStudentName(parent.getStudent().getName());
            response.setStudentEducationalLevel(parent.getStudent().getEducationalLevel());
        }
        return response;
    }
}