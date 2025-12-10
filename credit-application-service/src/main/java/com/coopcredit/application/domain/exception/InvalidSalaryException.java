package com.coopcredit.application.domain.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when salary validation fails.
 * This is a business exception in the domain layer.
 */
public class InvalidSalaryException extends RuntimeException {

    private final BigDecimal salario;

    public InvalidSalaryException(BigDecimal salario) {
        super(String.format("Invalid salary: %s. Salary must be greater than zero", salario));
        this.salario = salario;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
