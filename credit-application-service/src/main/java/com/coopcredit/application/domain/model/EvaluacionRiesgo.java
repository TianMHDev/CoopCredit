package com.coopcredit.application.domain.model;

import java.time.LocalDateTime;

/**
 * Pure domain model representing a risk evaluation.
 * This class is a POJO without any framework dependencies.
 */
public class EvaluacionRiesgo {

    private Long id;
    private Integer score;
    private String nivelRiesgo;
    private String detalle;
    private LocalDateTime fechaEvaluacion;

    /**
     * Default constructor
     */
    public EvaluacionRiesgo() {
    }

    /**
     * Constructor with all fields
     */
    public EvaluacionRiesgo(Long id, Integer score, String nivelRiesgo,
            String detalle, LocalDateTime fechaEvaluacion) {
        this.id = id;
        this.score = score;
        this.nivelRiesgo = nivelRiesgo;
        this.detalle = detalle;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
}
