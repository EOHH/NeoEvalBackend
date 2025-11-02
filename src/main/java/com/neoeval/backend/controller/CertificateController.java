package com.neoeval.backend.controller;

import com.neoeval.backend.dto.response.CertificateResponse;
import com.neoeval.backend.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificateById(@PathVariable Long id) {
        CertificateResponse response = certificateService.getCertificateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CertificateResponse>> getCertificatesByStudent(@PathVariable Long studentId) {
        List<CertificateResponse> responses = certificateService.getCertificatesByStudent(studentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<CertificateResponse>> getCertificatesByExam(@PathVariable Long examId) {
        List<CertificateResponse> responses = certificateService.getCertificatesByExam(examId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<CertificateResponse> verifyCertificate(@PathVariable String verificationCode) {
        CertificateResponse response = certificateService.verifyCertificate(verificationCode);
        return ResponseEntity.ok(response);
    }
}