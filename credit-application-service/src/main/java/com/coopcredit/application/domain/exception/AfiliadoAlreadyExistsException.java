package com.coopcredit.application.domain.exception;

/**
 * Exception thrown when an affiliate already exists with the same document.
 * This is a business exception in the domain layer.
 */
public class AfiliadoAlreadyExistsException extends RuntimeException {

    private final String documento;

    public AfiliadoAlreadyExistsException(String documento) {
        super(String.format("Affiliate with document '%s' already exists", documento));
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }
}
