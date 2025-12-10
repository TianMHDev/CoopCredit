package com.coopcredit.application.infrastructure.adapter.out.rest;

import com.coopcredit.application.domain.model.EvaluacionRiesgoResponse;
import com.coopcredit.application.domain.port.out.RiskCentralPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RiskCentralAdapter implements RiskCentralPort {

    private final RestTemplate restTemplate;

    @Value("${risk.central.url:http://localhost:8081/risk-evaluation}")
    private String riskCentralUrl;

    @Override
    public EvaluacionRiesgoResponse evaluateRisk(String documento, BigDecimal monto, int plazo) {
        Map<String, Object> request = new HashMap<>();
        request.put("documento", documento);
        request.put("monto", monto);
        request.put("plazo", plazo);

        return restTemplate.postForObject(riskCentralUrl, request, EvaluacionRiesgoResponse.class);
    }
}
