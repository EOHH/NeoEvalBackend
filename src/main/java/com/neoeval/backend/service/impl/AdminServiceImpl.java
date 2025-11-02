package com.neoeval.backend.service.impl;

import com.neoeval.backend.entity.Admin;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.AdminRepository;
import com.neoeval.backend.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    // ✅ CONSTRUCTOR CORRECTO (sin @RequiredArgsConstructor)
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con email: " + email));
    }

    @Override
    @Transactional
    public Admin createAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese email");
        }
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = getAdminById(id);

        admin.setName(adminDetails.getName());
        admin.setEmail(adminDetails.getEmail());
        admin.setAdminLevel(adminDetails.getAdminLevel());
        admin.setDepartment(adminDetails.getDepartment());
        admin.setCanManageUsers(adminDetails.isCanManageUsers());
        admin.setCanManageContent(adminDetails.isCanManageContent());
        admin.setCanManageSystem(adminDetails.isCanManageSystem());

        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admin> getAdminsByLevel(String level) {
        return adminRepository.findByAdminLevel(level);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admin> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}