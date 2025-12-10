package com.coopcredit.riskmock.service;

import com.coopcredit.riskmock.dto.RiskEvaluationRequest;
import com.coopcredit.riskmock.dto.RiskEvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service for deterministic risk evaluation.
 * Uses document hash as seed to ensure consistent results.
 */
@Service
public class RiskService {

    /**
     * Evaluates credit risk based on document number.
     * CRITICAL: Uses deterministic algorithm to ensure same document
     * always returns same risk evaluation.
     * 
     * @param request the evaluation request
     * @return the risk evaluation response
     */
    public RiskEvaluationResponse evaluateRisk(RiskEvaluationRequest request) {
        // Step 1: Generate seed from document (CRITICAL for consistency)
        long seed = Math.abs(request.getDocumento().hashCode() % 1000);

        // Step 2: Generate deterministic score using seed
        Random random = new Random(seed);
        int score = 300 + random.nextInt(651); // Range: 300-950

        // Step 3: Classify risk level based on score
        RiskClassification classification = classifyRisk(score);

        // Build response
        return new RiskEvaluationResponse(
                request.getDocumento(),
                score,
                classification.getNivelRiesgo(),
                classification.getDetalle());
    }

    /**
     * Classifies risk based on score.
     * 
     * @param score the credit score
     * @return the risk classification
     */
    private RiskClassification classifyRisk(int score) {
        if (score >= 300 && score <= 500) {
            return new RiskClassification(
                    "ALTO",
                    "High risk: Score indicates significant credit risk. Loan rejected.");
        } else if (score >= 501 && score <= 700) {
            return new RiskClassification(
                    "MEDIO",
                    "Medium risk: Score indicates moderate credit risk. Additional verification required.");
        } else { // 701-950
            return new RiskClassification(
                    "BAJO",
                    "Low risk: Score indicates good credit profile. Loan approved.");
        }
    }

    /**
     * Inner class to hold risk classification result.
     */
    private static class RiskClassification {
        private final String nivelRiesgo;
        private final String detalle;

        public RiskClassification(String nivelRiesgo, String detalle) {
            this.nivelRiesgo = nivelRiesgo;
            this.detalle = detalle;
        }

        public String getNivelRiesgo() {
            return nivelRiesgo;
        }

        public String getDetalle() {
            return detalle;
        }
    }
}
