package com.neoeval.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para indicar que un usuario no tiene permiso
 * para realizar una acción o acceder a un recurso.
 * Se mapea a un estado HTTP 403 (Forbidden).
 */
@ResponseStatus(HttpStatus.FORBIDDEN) // Envía un 403 Forbidden
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}