package com.neoeval.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Indica que esta excepción debería resultar en un 401 Unauthorized
public class AuthenticationException extends RuntimeException {

    // Constructor que acepta un mensaje String
    public AuthenticationException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException) con el mensaje
    }

    // Opcional: Si quieres un constructor que también acepte una causa
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}