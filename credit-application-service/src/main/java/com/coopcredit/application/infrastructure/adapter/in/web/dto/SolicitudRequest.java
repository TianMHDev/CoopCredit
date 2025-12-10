package com.coopcredit.application.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SolicitudRequest {

    @JsonProperty("documentoAfiliado")
    @NotBlank(message = "Affiliate document is required")
    private String documentoAfiliado;

    @JsonProperty("montoSolicitado")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "100.00", message = "Minimum amount is 100.00")
    private BigDecimal montoSolicitado;

    @JsonProperty("plazo")
    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Minimum term is 1 month")
    @Max(value = 60, message = "Maximum term is 60 months")
    private Integer plazo;

    @JsonProperty("tasaPropuesta")
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Rate must be positive")
    private BigDecimal tasaPropuesta;

    // Getters and Setters
    public String getDocumentoAfiliado() {
        return documentoAfiliado;
    }

    public void setDocumentoAfiliado(String documentoAfiliado) {
        this.documentoAfiliado = documentoAfiliado;
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
}
