package com.neoeval.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Long fieldValue;
    private String stringFieldValue;

    // Constructor que acepta los tres argumentos (String, String, Long)
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s no encontrada con %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.stringFieldValue = null;
    }

    // Constructor para aceptar un STRING como tercer argumento (3 argumentos)
    public ResourceNotFoundException(String resourceName, String fieldName, String stringFieldValue) {
        super(String.format("%s no encontrada con %s : '%s'", resourceName, fieldName, stringFieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.stringFieldValue = stringFieldValue;
        this.fieldValue = null;
    }

    // Constructor que acepta un mensaje adicional para Long (4 argumentos)
    public ResourceNotFoundException(String resourceName, String fieldName, Long fieldValue, String additionalMessage) {
        super(String.format("%s no encontrada con %s : '%s'. %s", resourceName, fieldName, fieldValue, additionalMessage));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.stringFieldValue = null;
    }

    // =========================================================================
    // ✅ NUEVO CONSTRUCTOR REQUERIDO: (String, String, String, String)
    // =========================================================================
    /**
     * Constructor que acepta un valor de campo de String y un mensaje adicional (4 argumentos).
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String stringFieldValue, String additionalMessage) {
        super(String.format("%s no encontrada con %s : '%s'. %s", resourceName, fieldName, stringFieldValue, additionalMessage));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.stringFieldValue = stringFieldValue;
        this.fieldValue = null;
    }
    // =========================================================================


    // Constructor para solo el mensaje
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
        this.stringFieldValue = null;
    }

    // Getters
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getFieldValue() {
        return fieldValue;
    }

    public String getStringFieldValue() {
        return stringFieldValue;
    }
}