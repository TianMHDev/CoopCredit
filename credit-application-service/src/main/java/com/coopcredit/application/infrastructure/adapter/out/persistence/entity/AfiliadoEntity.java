package com.coopcredit.application.infrastructure.adapter.out.persistence.entity;

import com.coopcredit.application.domain.model.EstadoAfiliado;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * JPA Entity for Affiliate persistence.
 * This entity is part of the infrastructure layer and contains JPA annotations.
 * It is mapped to/from the domain model using MapStruct.
 */
@Entity
@Table(name = "afiliados", uniqueConstraints = {
        @UniqueConstraint(name = "uk_afiliado_documento", columnNames = "documento")
})
public class AfiliadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "salario", nullable = false, precision = 15, scale = 2)
    private BigDecimal salario;

    @Column(name = "fecha_afiliacion", nullable = false)
    private LocalDate fechaAfiliacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAfiliado estado;

    @OneToMany(mappedBy = "afiliado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<SolicitudEntity> solicitudes = new java.util.ArrayList<>();

    /**
     * Default constructor required by JPA
     */
    public AfiliadoEntity() {
    }

    /**
     * Constructor with all fields
     */
    public AfiliadoEntity(Long id, String documento, String nombre, BigDecimal salario,
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AfiliadoEntity that = (AfiliadoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(documento, that.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documento);
    }

    @Override
    public String toString() {
        return "AfiliadoEntity{" +
                "id=" + id +
                ", documento='" + documento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", salario=" + salario +
                ", fechaAfiliacion=" + fechaAfiliacion +
                ", estado=" + estado +
                '}';
    }
}
