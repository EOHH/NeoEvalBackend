package com.neoeval.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Column(name = "admin_level", length = 50)
    private String adminLevel = "BASIC"; // BASIC, INTERMEDIATE, SUPER

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "can_manage_users")
    private boolean canManageUsers = true;

    @Column(name = "can_manage_content")
    private boolean canManageContent = true;

    @Column(name = "can_manage_system")
    private boolean canManageSystem = false;

    // Constructores
    public Admin() {
        super();
        this.setUserType("ADMIN");
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
    }

    public Admin(String name, String email, String password, String adminLevel, String department) {
        super(name, email, password, "ADMIN");
        this.adminLevel = adminLevel;
        this.department = department;
    }

    // Getters y Setters
    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }

    public boolean isCanManageContent() {
        return canManageContent;
    }

    public void setCanManageContent(boolean canManageContent) {
        this.canManageContent = canManageContent;
    }

    public boolean isCanManageSystem() {
        return canManageSystem;
    }

    public void setCanManageSystem(boolean canManageSystem) {
        this.canManageSystem = canManageSystem;
    }
}