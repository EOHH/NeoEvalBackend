// Archivo: com.neoeval.backend.NeoEvalBackendApplication.java

// 1. ✅ Asegura que el paquete sea correcto
package com.neoeval.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;
// 2. ✅ IMPORTACIÓN CORREGIDA para Spring Boot 3 / Jakarta
import jakarta.annotation.PostConstruct;


@SpringBootApplication
public class NeoEvalBackendApplication {

    // 🚀 Configuración definitiva para la zona horaria de Perú
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    }

    public static void main(String[] args) {
        // No es necesario llamar a TimeZone.setDefault(TimeZone.getTimeZone("UTC")) aquí.
        SpringApplication.run(NeoEvalBackendApplication.class, args);
    }
}