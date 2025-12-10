package com.coopcredit.application.infrastructure.adapter.in.web.dto;

import com.coopcredit.application.domain.model.EstadoAfiliado;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for affiliate registration request.
 * Contains validation annotations for input validation.
 */
public class AfiliadoRegisterRequest {

    @NotBlank(message = "Document is required")
    @Size(min = 5, max = 20, message = "Document must be between 5 and 20 characters")
    private String documento;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String nombre;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
    private BigDecimal salario;

    private LocalDate fechaAfiliacion;

    private EstadoAfiliado estado;

    // Constructors
    public AfiliadoRegisterRequest() {
    }

    public AfiliadoRegisterRequest(String documento, String nombre, BigDecimal salario,
            LocalDate fechaAfiliacion, EstadoAfiliado estado) {
        this.documento = documento;
        this.nombre = nombre;
        this.salario = salario;
        this.fechaAfiliacion = fechaAfiliacion;
        this.estado = estado;
    }

    // Getters and Setters - IMPORTANT: These are required for Jackson
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

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

    public LocalDate getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(LocalDate fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public EstadoAfiliado getEstado() {
        return estado;
    }

    public void setEstado(EstadoAfiliado estado) {
        this.estado = estado;
    }
}
