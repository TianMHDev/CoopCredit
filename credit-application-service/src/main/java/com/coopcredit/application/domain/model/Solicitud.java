package com.coopcredit.application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pure domain model representing a credit application.
 * This class is a POJO without any framework dependencies.
 */
public class Solicitud {

    private Long id;
    private Afiliado afiliado;
    private BigDecimal montoSolicitado;
    private Integer plazo;
    private BigDecimal tasaPropuesta;
    private LocalDate fechaSolicitud;
    private EstadoSolicitud estado;
    private EvaluacionRiesgo evaluacion;

    /**
     * Default constructor
     */
    public Solicitud() {
    }

    /**
     * Constructor with all fields
     */
    public Solicitud(Long id, Afiliado afiliado, BigDecimal montoSolicitado,
            Integer plazo, BigDecimal tasaPropuesta, LocalDate fechaSolicitud,
            EstadoSolicitud estado, EvaluacionRiesgo evaluacion) {
        this.id = id;
        this.afiliado = afiliado;
        this.montoSolicitado = montoSolicitado;
        this.plazo = plazo;
        this.tasaPropuesta = tasaPropuesta;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.evaluacion = evaluacion;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getTasaPropuesta() {
        return tasaPropuesta;
    }

    public void setTasaPropuesta(BigDecimal tasaPropuesta) {
        this.tasaPropuesta = tasaPropuesta;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public EvaluacionRiesgo getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(EvaluacionRiesgo evaluacion) {
        this.evaluacion = evaluacion;
    }
}
