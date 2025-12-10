package com.coopcredit.riskmock.controller;

import com.coopcredit.riskmock.dto.RiskEvaluationRequest;
import com.coopcredit.riskmock.dto.RiskEvaluationResponse;
import com.coopcredit.riskmock.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for risk evaluation.
 */
@RestController
@RequestMapping("/risk-evaluation")
public class RiskEvaluationController {

    private final RiskService riskService;

    public RiskEvaluationController(RiskService riskService) {
        this.riskService = riskService;
    }

    /**
     * Evaluates credit risk for an affiliate.
     * 
     * @param request the evaluation request
     * @return the risk evaluation response
     */
    @PostMapping
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(
            @RequestBody RiskEvaluationRequest request) {

        RiskEvaluationResponse response = riskService.evaluateRisk(request);
        return ResponseEntity.ok(response);
    }
}
