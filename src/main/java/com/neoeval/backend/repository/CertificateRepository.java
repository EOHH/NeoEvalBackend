package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Certificate; // Asegúrate de que tu entidad Certificate exista
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Para findByVerificationCode

@Repository // <-- ¡Es crucial!
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // Método para obtener certificados por el ID del estudiante
    List<Certificate> findByStudentId(Long studentId); // <-- Añadido

    // Método para obtener certificados por el ID del examen
    List<Certificate> findByExamId(Long examId); // <-- Necesario para getCertificatesByExam

    // Método para verificar un certificado por su código
    Optional<Certificate> findByVerificationCode(String verificationCode); // <-- Necesario para verifyCertificate
}