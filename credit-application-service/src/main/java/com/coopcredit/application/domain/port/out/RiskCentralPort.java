package com.coopcredit.application.domain.port.out;

import com.coopcredit.application.domain.model.EvaluacionRiesgoResponse;

import java.math.BigDecimal;

/**
 * Output port for Risk Central service integration.
 * This interface defines the contract for external risk evaluation
 * without depending on any specific implementation.
 */
public interface RiskCentralPort {

    /**
     * Evaluates the credit risk for an affiliate.
     * 
     * @param documento the affiliate's document number
     * @param monto     the requested amount
     * @param plazo     the requested term in months
     * @return the risk evaluation response from the external service
     */
    EvaluacionRiesgoResponse evaluateRisk(String documento, BigDecimal monto, int plazo);
}
