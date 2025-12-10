package com.coopcredit.application.infrastructure.adapter.in.web.dto;

import com.coopcredit.application.domain.model.EstadoAfiliado;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO for affiliate update request.
 * All fields are optional for partial updates.
 */
public class AfiliadoUpdateRequest {

    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String nombre;

    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
    private BigDecimal salario;

    private EstadoAfiliado estado;

    /**
     * Default constructor
     */
    public AfiliadoUpdateRequest() {
    }

    /**
     * Constructor with all fields
     */
    public AfiliadoUpdateRequest(String nombre, BigDecimal salario, EstadoAfiliado estado) {
        this.nombre = nombre;
        this.salario = salario;
        this.estado = estado;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public EstadoAfiliado getEstado() {
        return estado;
    }

    public void setEstado(EstadoAfiliado estado) {
        this.estado = estado;
    }
}
