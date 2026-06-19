package com.neoeval.backend.config;

import com.neoeval.backend.entity.Admin;
import com.neoeval.backend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @org.springframework.beans.factory.annotation.Value("${app.admin.default-email}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${app.admin.default-password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin superAdmin = new Admin();
                superAdmin.setName("Super Administrador");
                superAdmin.setEmail(adminEmail);
                superAdmin.setPassword(passwordEncoder.encode(adminPassword));
                superAdmin.setAdminLevel("SUPER_ADMIN");
                superAdmin.setDepartment("Sistema");
                superAdmin.setCanManageUsers(true);
                superAdmin.setCanManageContent(true);
                superAdmin.setCanManageSystem(true);

                // ✅ Asegurar que el admin esté aprobado desde el inicio
                superAdmin.setApprovalStatus("APPROVED");
                
                // ✅ Administrador verificado por defecto
                superAdmin.setEmailVerified(true);

                adminRepository.save(superAdmin);
                System.out.println("✅ Super admin creado y APROBADO: " + adminEmail);
            }
        };
    }
}