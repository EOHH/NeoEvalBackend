package com.neoeval.backend.service;

import com.neoeval.backend.dto.response.CertificateResponse;
import java.util.List;

public interface CertificateService {
    CertificateResponse getCertificateById(Long id);
    List<CertificateResponse> getCertificatesByStudent(Long studentId);
    List<CertificateResponse> getCertificatesByExam(Long examId); // Ya está aquí
    CertificateResponse verifyCertificate(String verificationCode); // Ya está aquí
}