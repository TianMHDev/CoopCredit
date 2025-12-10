package com.coopcredit.application.domain.model;

/**
 * Response from the Risk Central service.
 * This is a domain model representing the external service response.
 */
public class EvaluacionRiesgoResponse {

    private Integer score;
    private String nivelRiesgo;
    private String detalle;

    /**
     * Default constructor
     */
    public EvaluacionRiesgoResponse() {
    }

    /**
     * Constructor with all fields
     */
    public EvaluacionRiesgoResponse(Integer score, String nivelRiesgo, String detalle) {
        this.score = score;
        this.nivelRiesgo = nivelRiesgo;
        this.detalle = detalle;
    }

    // Getters and Setters
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
}
