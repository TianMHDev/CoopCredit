package com.coopcredit.application.domain.exception;

/**
 * Exception thrown when an affiliate is not in ACTIVE status.
 */
public class AfiliadoInactivoException extends RuntimeException {

    public AfiliadoInactivoException(String documento) {
        super("Affiliate with document '" + documento + "' is not in ACTIVE status");
    }
}
