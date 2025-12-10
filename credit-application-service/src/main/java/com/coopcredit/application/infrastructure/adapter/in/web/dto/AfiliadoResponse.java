package com.coopcredit.application.infrastructure.adapter.in.web.dto;

import com.coopcredit.application.domain.model.EstadoAfiliado;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for affiliate response.
 * Used to return affiliate data to clients.
 */
public class AfiliadoResponse {

    private Long id;
    private String documento;
    private String nombre;
    private BigDecimal salario;
    private LocalDate fechaAfiliacion;
    private EstadoAfiliado estado;

    /**
     * Default constructor
     */
    public AfiliadoResponse() {
    }

    /**
     * Constructor with all fields
     */
    public AfiliadoResponse(Long id, String documento, String nombre, BigDecimal salario,
            LocalDate fechaAfiliacion, EstadoAfiliado estado) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.salario = salario;
        this.fechaAfiliacion = fechaAfiliacion;
        this.estado = estado;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
