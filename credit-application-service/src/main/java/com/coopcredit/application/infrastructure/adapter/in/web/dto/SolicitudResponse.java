package com.coopcredit.application.infrastructure.adapter.in.web.dto;

import com.coopcredit.application.domain.model.EstadoSolicitud;
import com.coopcredit.application.domain.model.EvaluacionRiesgo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SolicitudResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("documentoAfiliado")
    private String documentoAfiliado;

    @JsonProperty("nombreAfiliado")
    private String nombreAfiliado;

    @JsonProperty("montoSolicitado")
    private BigDecimal montoSolicitado;

    @JsonProperty("plazo")
    private Integer plazo;

    @JsonProperty("tasaPropuesta")
    private BigDecimal tasaPropuesta;

    @JsonProperty("fechaSolicitud")
    private LocalDate fechaSolicitud;

    @JsonProperty("estado")
    private EstadoSolicitud estado;

    @JsonProperty("evaluacion")
    private EvaluacionRiesgo evaluacion;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentoAfiliado() {
        return documentoAfiliado;
    }

    public void setDocumentoAfiliado(String documentoAfiliado) {
        this.documentoAfiliado = documentoAfiliado;
    }

    public String getNombreAfiliado() {
        return nombreAfiliado;
    }

    public void setNombreAfiliado(String nombreAfiliado) {
        this.nombreAfiliado = nombreAfiliado;
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
