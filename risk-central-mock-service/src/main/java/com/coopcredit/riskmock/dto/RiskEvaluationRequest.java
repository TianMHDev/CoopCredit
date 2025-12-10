package com.coopcredit.riskmock.dto;

import java.math.BigDecimal;

/**
 * Request DTO for risk evaluation.
 */
public class RiskEvaluationRequest {

    private String documento;
    private BigDecimal monto;
    private Integer plazo;

    public RiskEvaluationRequest() {
    }

    public RiskEvaluationRequest(String documento, BigDecimal monto, Integer plazo) {
        this.documento = documento;
        this.monto = monto;
        this.plazo = plazo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }
}
