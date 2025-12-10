package com.coopcredit.application.domain.exception;

/**
 * Exception thrown when an affiliate is not found.
 * This is a business exception in the domain layer.
 */
public class AfiliadoNotFoundException extends RuntimeException {

    private Long id;
    private String documento;

    public AfiliadoNotFoundException(Long id) {
        super(String.format("Affiliate with ID '%d' not found", id));
        this.id = id;
    }

    public AfiliadoNotFoundException(String documento) {
        super(String.format("Affiliate with document '%s' not found", documento));
        this.documento = documento;
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }
}
