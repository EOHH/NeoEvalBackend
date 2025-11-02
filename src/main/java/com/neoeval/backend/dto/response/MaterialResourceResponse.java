package com.neoeval.backend.dto.response;

import java.time.Instant;

public class MaterialResourceResponse {

    private Long id;
    private String resourceName;
    private String resourceType;
    private String storagePath;
    private Integer fileSizeKB;
    private Instant uploadedAt;

    // Constructor desde la entidad (opcional, pero útil)
    /*
    public MaterialResourceResponse(MaterialResource entity) {
        this.id = entity.getId();
        // ... mapeo de campos ...
    }
    */

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
}