package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.LoginRequest;
import com.neoeval.backend.dto.request.RegisterRequest;
import com.neoeval.backend.dto.response.AuthResponse;
import com.neoeval.backend.entity.Parent;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.entity.Teacher;
import com.neoeval.backend.entity.User;
import com.neoeval.backend.exception.AuthenticationException;
import com.neoeval.backend.repository.UserRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.repository.ParentRepository;
import com.neoeval.backend.repository.TeacherRepository;
import com.neoeval.backend.security.JwtTokenProvider;
import com.neoeval.backend.security.UserPrincipal;
import com.neoeval.backend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    // ... (El constructor no cambia) ...
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            TeacherRepository teacherRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado después de autenticación exitosa"));

        user.setLastLogin(Instant.now());
        userRepository.save(user);

        String jwt = tokenProvider.generateToken(userPrincipal);

        AuthResponse authResponse = new AuthResponse(jwt, user.getId(), user.getUserType(), user.getName(), user.getEmail());

        if ("PARENT".equals(user.getUserType())) {
            Parent parent = parentRepository.findById(user.getId())
                    .orElseThrow(() -> new AuthenticationException("Datos de Padre no encontrados para el usuario: " + user.getId()));

            if (parent.getStudent() != null) {
                authResponse.setStudentId(parent.getStudent().getId());
            }
        }

        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse registerUser(RegisterRequest registerRequest) {
        // ... (Toda la lógica del switch (case "STUDENT", "PARENT", "TEACHER") no cambia) ...
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException("El correo ya está registrado");
        }

        User user;
        switch (registerRequest.getUserType().toUpperCase()) {
            case "STUDENT":
                Student student = new Student();
                student.setName(registerRequest.getName());
                student.setEmail(registerRequest.getEmail());
                student.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                student.setUserType("STUDENT");
                student.setEducationalLevel(registerRequest.getEducationalLevel());
                student.setBirthDate(registerRequest.getBirthDate());
                user = studentRepository.save(student);
                break;
            case "PARENT":
                Parent parent = new Parent();
                parent.setName(registerRequest.getName());
                parent.setEmail(registerRequest.getEmail());
                parent.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                parent.setUserType("PARENT");
                parent.setRelationship(registerRequest.getRelationship());

                if (registerRequest.getStudentId() != null) {
                    Student linkedStudent = studentRepository.findById(registerRequest.getStudentId())
                            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + registerRequest.getStudentId()));
                    parent.setStudent(linkedStudent);
                }

                user = parentRepository.save(parent);
                break;
            case "TEACHER":
                Teacher teacher = new Teacher();
                teacher.setName(registerRequest.getName());
                teacher.setEmail(registerRequest.getEmail());
                teacher.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                teacher.setUserType("TEACHER");
                teacher.setDepartment(registerRequest.getDepartment());
                teacher.setExpertiseArea(registerRequest.getExpertiseArea());
                user = teacherRepository.save(teacher);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario inválido: " + registerRequest.getUserType());
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String jwt = tokenProvider.generateToken(userPrincipal);

        AuthResponse authResponse = new AuthResponse(jwt, user.getId(), user.getUserType(), user.getName(), user.getEmail());

        if ("PARENT".equals(user.getUserType()) && user instanceof Parent) {
            Parent registeredParent = (Parent) user;
            if (registeredParent.getStudent() != null) {
                authResponse.setStudentId(registeredParent.getStudent().getId());
            }
        }

        return authResponse;
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Token de refresco inválido");
        }
        Long userId = tokenProvider.getUserIdFromJWT(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado para refrescar token"));

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newToken = tokenProvider.generateToken(userPrincipal);

        AuthResponse authResponse = new AuthResponse(newToken, user.getId(), user.getUserType(), user.getName(), user.getEmail());

        if ("PARENT".equals(user.getUserType())) {
            Parent parent = parentRepository.findById(user.getId())
                    .orElseThrow(() -> new AuthenticationException("Datos de Padre no encontrados para el usuario: " + user.getId()));

            if (parent.getStudent() != null) {
                authResponse.setStudentId(parent.getStudent().getId());
            }
        }

        return authResponse;
    }

    @Override
    public void logoutUser(String token) {
        logger.info("El token JWT {} ha sido invalidado (implementación de logout pendiente).", token);
    }
}