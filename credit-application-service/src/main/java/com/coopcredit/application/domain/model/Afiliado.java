package com.coopcredit.application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Pure domain model representing an Affiliate.
 * This is a POJO without any framework annotations (no @Entity, @Id, etc.).
 * It contains only business logic and domain rules.
 */
public class Afiliado {
    
    private Long id;
    private String documento;
    private String nombre;
    private BigDecimal salario;
    private LocalDate fechaAfiliacion;
    private EstadoAfiliado estado;

    /**
     * Default constructor
     */
    public Afiliado() {
    }

    /**
     * Constructor with all fields
     */
    public Afiliado(Long id, String documento, String nombre, BigDecimal salario, 
                    LocalDate fechaAfiliacion, EstadoAfiliado estado) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.salario = salario;
        this.fechaAfiliacion = fechaAfiliacion;
        this.estado = estado;
    }

    /**
     * Constructor without ID (for new affiliates)
     */
    public Afiliado(String documento, String nombre, BigDecimal salario, 
                    LocalDate fechaAfiliacion, EstadoAfiliado estado) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Afiliado afiliado = (Afiliado) o;
        return Objects.equals(documento, afiliado.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return "Afiliado{" +
                "id=" + id +
                ", documento='" + documento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", salario=" + salario +
                ", fechaAfiliacion=" + fechaAfiliacion +
                ", estado=" + estado +
                '}';
    }
}
