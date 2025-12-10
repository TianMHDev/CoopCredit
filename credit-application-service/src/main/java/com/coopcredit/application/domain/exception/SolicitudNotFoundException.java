package com.coopcredit.application.domain.exception;

/**
 * Exception thrown when a credit application is not found.
 */
public class SolicitudNotFoundException extends RuntimeException {

    public SolicitudNotFoundException(Long id) {
        super("Credit application with ID '" + id + "' not found");
    }
}
