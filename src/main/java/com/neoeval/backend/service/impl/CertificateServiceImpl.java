package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.response.CertificateResponse;
import com.neoeval.backend.entity.Certificate;
import com.neoeval.backend.entity.Student;
import com.neoeval.backend.entity.Exam;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.CertificateRepository;
import com.neoeval.backend.repository.StudentRepository;
import com.neoeval.backend.repository.ExamRepository;
import com.neoeval.backend.service.CertificateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneOffset; // 👈 NECESARIO para la conversión de LocalDateTime a Instant

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public CertificateServiceImpl(
            CertificateRepository certificateRepository,
            StudentRepository studentRepository,
            ExamRepository examRepository
    ) {
        this.certificateRepository = certificateRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    @Override
    public CertificateResponse getCertificateById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado", "id", id));
        return mapToCertificateResponse(certificate);
    }

    @Override
    public List<CertificateResponse> getCertificatesByStudent(Long studentId) {
        // Validación opcional (dejada como comentario por ahora)
        // studentRepository.findById(studentId)
        //         .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", studentId));

        return certificateRepository.findByStudentId(studentId).stream()
                .map(this::mapToCertificateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificateResponse> getCertificatesByExam(Long examId) {
        // Validación opcional (dejada como comentario por ahora)
        // examRepository.findById(examId)
        //         .orElseThrow(() -> new ResourceNotFoundException("Examen", "id", examId));

        return certificateRepository.findByExamId(examId).stream()
                .map(this::mapToCertificateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateResponse verifyCertificate(String verificationCode) {
        Certificate certificate = certificateRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado", "código de verificación", verificationCode));
        return mapToCertificateResponse(certificate);
    }

    // Método de mapeo centralizado
    private CertificateResponse mapToCertificateResponse(Certificate certificate) {
        CertificateResponse response = new CertificateResponse();
        response.setId(certificate.getId());
        response.setTitle(certificate.getTitle());

        // 💥 CORRECCIÓN: Conversión de LocalDateTime (Entity) a Instant (DTO)
        if (certificate.getIssueDate() != null) {
            response.setIssueDate(certificate.getIssueDate().toInstant(ZoneOffset.UTC));
        }

        response.setVerificationCode(certificate.getVerificationCode());

        if (certificate.getStudent() != null) {
            response.setStudentId(certificate.getStudent().getId());
            response.setStudentName(certificate.getStudent().getName());
        } else {
            response.setStudentId(null);
            response.setStudentName("N/A");
        }

        if (certificate.getExam() != null) {
            response.setExamId(certificate.getExam().getId());
            response.setExamTitle(certificate.getExam().getTitle());
        } else {
            response.setExamId(null);
            response.setExamTitle("N/A");
        }
        return response;
    }

    // *** Opcional: Si necesitas un método para crear certificados ***
    // Se mantiene comentado, pero recuerda usar Instant.now() si lo implementas
    /*
    @Transactional
    public CertificateResponse createCertificate(CreateCertificateRequest request) {
        // ... (código para buscar student y exam) ...

        Certificate certificate = new Certificate();
        // ...
        certificate.setIssueDate(Instant.now()); // Usa Instant.now() si la entidad espera Instant
        // ...
        return mapToCertificateResponse(savedCertificate);
    }
    */
}