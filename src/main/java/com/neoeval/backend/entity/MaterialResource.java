package com.neoeval.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "material_resources")
public class MaterialResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_name", nullable = false, length = 255)
    private String resourceName;

    @Column(name = "resource_type", nullable = false, length = 50)
    private String resourceType; // PPT, PDF, VIDEO, LINK, DOC

    @Column(name = "storage_path", nullable = false, length = 512)
    private String storagePath; // Ruta S3, URL, etc.

    @Column(name = "file_size_kb")
    private Integer fileSizeKB;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt = Instant.now();

    // 🔗 RELACIÓN M:1 con ClassSession (La columna 'session_id' se crea aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ClassSession classSession;

    // Constructores
    public MaterialResource() {}

    public MaterialResource(String resourceName, String resourceType, String storagePath, ClassSession classSession) {
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.storagePath = storagePath;
        this.classSession = classSession;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    public Integer getFileSizeKB() { return fileSizeKB; }
    public void setFileSizeKB(Integer fileSizeKB) { this.fileSizeKB = fileSizeKB; }
    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }
    public ClassSession getClassSession() { return classSession; }
    public void setClassSession(ClassSession classSession) { this.classSession = classSession; }
}