package com.neoeval.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de Verificación - NeoEval");
        message.setText("Bienvenido a NeoEval.\n\n" +
                "Para completar tu registro, por favor ingresa el siguiente código de 6 dígitos en la aplicación:\n\n" +
                "Código OTP: " + otp + "\n\n" +
                "Este código expirará en 15 minutos.\n\n" +
                "Si no solicitaste este código, puedes ignorar este correo.");
        
        mailSender.send(message);
    }
}
