package com.neoeval.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull; // Mantener si lo usas en otros campos

public class MaterialResourceRequest {

    // resourceName y resourceType deben ser obligatorios en todos los casos
    @NotBlank(message = "El nombre del recurso es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String resourceName;

    @NotBlank(message = "El tipo de recurso es obligatorio (LINK, VIDEO, etc.)")
    @Size(max = 50, message = "El tipo de recurso no puede exceder 50 caracteres")
    private String resourceType;

    // 💥 CORRECCIÓN: ELIMINAR @NotBlank. DEBE PERMITIR NULL O CADENA VACÍA.
    @Size(max = 512, message = "La ruta o URL no puede exceder 512 caracteres")
    private String storagePath;

    // ✅ fileSizeKB es opcional. Integer por defecto permite null.
    private Integer fileSizeKB;

    // Getters y Setters...
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }
    public Integer getFileSizeKB() { return fileSizeKB; }
    public void setFileSizeKB(Integer fileSizeKB) { this.fileSizeKB = fileSizeKB; }
}