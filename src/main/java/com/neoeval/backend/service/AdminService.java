package com.neoeval.backend.service;

import com.neoeval.backend.entity.Admin;
import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    Admin getAdminById(Long id);
    Admin getAdminByEmail(String email);
    Admin createAdmin(Admin admin);
    Admin updateAdmin(Long id, Admin adminDetails);
    void deleteAdmin(Long id);
    List<Admin> getAdminsByLevel(String level);
    List<Admin> getAdminsByDepartment(String department);
    boolean existsByEmail(String email);
}