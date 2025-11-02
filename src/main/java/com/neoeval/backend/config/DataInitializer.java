package com.neoeval.backend.config;

import com.neoeval.backend.entity.Admin;
import com.neoeval.backend.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existe un admin
            if (adminRepository.count() == 0) {
                Admin superAdmin = new Admin();
                superAdmin.setName("Super Administrador");
                superAdmin.setEmail("super.admin@neoeval.edu");
                superAdmin.setPassword(passwordEncoder.encode("Admin123!"));
                superAdmin.setAdminLevel("SUPER_ADMIN");
                superAdmin.setDepartment("Sistema");
                superAdmin.setCanManageUsers(true);
                superAdmin.setCanManageContent(true);
                superAdmin.setCanManageSystem(true);

                adminRepository.save(superAdmin);
                System.out.println("✅ Super admin creado: super.admin@neoeval.edu / Admin123!");
            }
        };
    }
}