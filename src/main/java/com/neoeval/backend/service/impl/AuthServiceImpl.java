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
import com.neoeval.backend.entity.EmailVerificationToken;
import com.neoeval.backend.repository.EmailVerificationTokenRepository;
import com.neoeval.backend.service.EmailService;
import com.neoeval.backend.util.EmailDomainValidator;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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
    private final EmailService emailService;
    private final EmailVerificationTokenRepository tokenRepository;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository,
            TeacherRepository teacherRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider,
            EmailService emailService,
            EmailVerificationTokenRepository tokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        // ✅ PASO 1: Buscar usuario ANTES de autenticar para validar aprobación
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("Credenciales inválidas"));

        // ✅ PASO 2: VALIDAR ESTADO DE VERIFICACIÓN Y APROBACIÓN
        if (!user.isEmailVerified()) {
            throw new AuthenticationException("Debes verificar tu correo electrónico antes de iniciar sesión.");
        }

        if (!"APPROVED".equals(user.getApprovalStatus())) {
            if ("PENDING".equals(user.getApprovalStatus())) {
                throw new AuthenticationException(
                        "Tu cuenta está pendiente de aprobación. Un administrador debe revisar tu solicitud antes de que puedas iniciar sesión."
                );
            } else if ("REJECTED".equals(user.getApprovalStatus())) {
                String reason = user.getRejectionReason() != null
                        ? " Razón: " + user.getRejectionReason()
                        : "";
                throw new AuthenticationException(
                        "Tu cuenta ha sido rechazada." + reason
                );
            }
        }

        // ✅ PASO 3: Autenticar (solo si está APPROVED)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Actualizar último login
        user.setLastLogin(Instant.now());
        userRepository.save(user);

        // Generar token
        String jwt = tokenProvider.generateToken(userPrincipal);

        // Crear respuesta
        AuthResponse authResponse = new AuthResponse(
                jwt,
                user.getId(),
                user.getUserType(),
                user.getName(),
                user.getEmail()
        );

        // ✅ AGREGAR approval_status a la respuesta
        authResponse.setApprovalStatus(user.getApprovalStatus());

        // Si es PARENT, agregar studentId
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
        // Validar que el dominio del correo tenga servidores habilitados (MX Records)
        String email = registerRequest.getEmail();
        if (email != null && email.contains("@")) {
            String domain = email.substring(email.lastIndexOf("@") + 1);
            if (!EmailDomainValidator.hasMXRecord(domain)) {
                throw new AuthenticationException("El dominio del correo no existe o no puede recibir correos. Ingresa un correo válido.");
            }
        }

        // Validar que el email no exista
        if (userRepository.existsByEmail(email)) {
            throw new AuthenticationException("El correo ya está registrado");
        }

        User user;
        String userType = registerRequest.getUserType().toUpperCase();

        // ✅ Crear usuario según el tipo (con approval_status = PENDING por defecto)
        switch (userType) {
            case "STUDENT":
                Student student = new Student();
                student.setName(registerRequest.getName());
                student.setEmail(registerRequest.getEmail());
                student.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                student.setUserType("STUDENT");
                student.setEducationalLevel(registerRequest.getEducationalLevel());
                student.setBirthDate(registerRequest.getBirthDate());
                // approval_status se establece en "PENDING" automáticamente por el constructor de User
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
                throw new IllegalArgumentException("Tipo de usuario inválido: " + userType);
        }

        // ✅ GENERAR Y ENVIAR OTP
        String otp = generateOtp();
        EmailVerificationToken verificationToken = new EmailVerificationToken(otp, user, LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(verificationToken);
        
        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
        } catch (Exception e) {
            logger.error("Error enviando el correo de verificación a {}: {}", user.getEmail(), e.getMessage());
            // No lanzar excepción para que no falle el registro si hay un error temporal con SMTP
        }

        // ✅ NO GENERAR TOKEN - El usuario debe esperar aprobación
        // Retornar respuesta indicando que está pendiente
        AuthResponse authResponse = new AuthResponse(
                null, // Sin token
                user.getId(),
                user.getUserType(),
                user.getName(),
                user.getEmail()
        );

        // ✅ Indicar que está pendiente de aprobación
        authResponse.setApprovalStatus("PENDING");

        // Si es PARENT y tiene estudiante vinculado, agregarlo
        if ("PARENT".equals(user.getUserType()) && user instanceof Parent) {
            Parent registeredParent = (Parent) user;
            if (registeredParent.getStudent() != null) {
                authResponse.setStudentId(registeredParent.getStudent().getId());
            }
        }

        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Token de refresco inválido");
        }

        Long userId = tokenProvider.getUserIdFromJWT(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado para refrescar token"));

        // ✅ VALIDAR ESTADO DE APROBACIÓN también en refresh
        if (!"APPROVED".equals(user.getApprovalStatus())) {
            throw new AuthenticationException("Tu cuenta ya no está aprobada. Contacta al administrador.");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newToken = tokenProvider.generateToken(userPrincipal);

        AuthResponse authResponse = new AuthResponse(
                newToken,
                user.getId(),
                user.getUserType(),
                user.getName(),
                user.getEmail()
        );

        // ✅ Agregar approval_status
        authResponse.setApprovalStatus(user.getApprovalStatus());

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
        // Aquí puedes implementar una blacklist de tokens si lo necesitas
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Usuario no encontrado con ese correo."));

        if (user.isEmailVerified()) {
            throw new AuthenticationException("El correo ya se encuentra verificado.");
        }

        EmailVerificationToken token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new AuthenticationException("No se encontró un código de verificación para este usuario."));

        if (!token.getToken().equals(otp)) {
            throw new AuthenticationException("El código OTP es incorrecto.");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("El código OTP ha expirado.");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        
        tokenRepository.deleteByUser(user);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(900000) + 100000;
        return String.valueOf(num);
    }
}
