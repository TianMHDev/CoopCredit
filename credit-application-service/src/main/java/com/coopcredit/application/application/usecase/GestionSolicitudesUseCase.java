package com.coopcredit.application.application.usecase;

import com.coopcredit.application.domain.exception.AfiliadoInactivoException;
import com.coopcredit.application.domain.exception.AfiliadoNotFoundException;
import com.coopcredit.application.domain.model.*;
import com.coopcredit.application.domain.port.in.SolicitudServicePort;
import com.coopcredit.application.domain.port.out.AfiliadoPersistencePort;
import com.coopcredit.application.domain.port.out.RiskCentralPort;
import com.coopcredit.application.domain.port.out.SolicitudPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GestionSolicitudesUseCase implements SolicitudServicePort {

    private final SolicitudPersistencePort solicitudPersistencePort;
    private final AfiliadoPersistencePort afiliadoPersistencePort;
    private final RiskCentralPort riskCentralPort;

    @Override
    @Transactional
    public Solicitud registrarSolicitud(Solicitud solicitud) {
        // 0. Security: Validate that if AFFILIATE, only register for themselves
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AFILIADO"))) {
            String username = auth.getName();
            if (!username.equals(solicitud.getAfiliado().getDocumento())) {
                throw new AccessDeniedException(
                        "You do not have permission to register requests on behalf of another affiliate.");
            }
        }

        // 1. Validate Affiliate exists and is active
        Afiliado afiliado = afiliadoPersistencePort.findByDocumento(solicitud.getAfiliado().getDocumento())
                .orElseThrow(() -> new AfiliadoNotFoundException(solicitud.getAfiliado().getDocumento()));

        if (!"ACTIVO".equalsIgnoreCase(afiliado.getEstado().name())) {
            throw new AfiliadoInactivoException("The affiliate is not active and cannot apply for credits.");
        }

        // 2. Set initial state and link affiliate
        solicitud.setAfiliado(afiliado);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setFechaSolicitud(LocalDate.now());

        // STEP 1: Affiliate registers a request (PENDING state)
        Solicitud solicitudGuardada = solicitudPersistencePort.save(solicitud);
        System.out.println(
                "FLOW [1/6]: Request initially saved with PENDING state. ID: " + solicitudGuardada.getId());

        // STEP 2: System invokes risk-central-mock-service
        System.out.println("FLOW [2/6]: Invoking external risk service...");
        EvaluacionRiesgoResponse riskResponse = riskCentralPort.evaluateRisk(
                afiliado.getDocumento(),
                solicitud.getMontoSolicitado(),
                solicitud.getPlazo());

        // STEP 3: Receive score and risk level
        System.out.println("FLOW [3/6]: Response received. Score: " + riskResponse.getScore() + ", Level: "
                + riskResponse.getNivelRiesgo());

        // 5. Create Risk Evaluation domain object
        EvaluacionRiesgo evaluacion = new EvaluacionRiesgo();
        evaluacion.setScore(riskResponse.getScore());
        evaluacion.setNivelRiesgo(riskResponse.getNivelRiesgo());
        evaluacion.setDetalle(riskResponse.getDetalle());
        evaluacion.setFechaEvaluacion(LocalDateTime.now());

        solicitudGuardada.setEvaluacion(evaluacion);

        // STEP 4 & 5: Apply internal policies and Evaluate Request
        System.out.println("FLOW [4/6]: Applying internal policies...");
        aplicarPoliticasYDecidir(solicitudGuardada, riskResponse);
        System.out.println("FLOW [5/6]: Decision made: " + solicitudGuardada.getEstado());

        // STEP 6: Update request (Transactional)
        Solicitud resultado = solicitudPersistencePort.save(solicitudGuardada);
        System.out.println("FLOW [6/6]: Request updated and finalized successfully.");

        return resultado;
    }

    private void aplicarPoliticasYDecidir(Solicitud solicitud, EvaluacionRiesgoResponse riskResponse) {
        StringBuilder motivosRechazo = new StringBuilder();
        boolean rechazado = false;

        // 1. External Risk Policy
        if ("ALTO".equalsIgnoreCase(riskResponse.getNivelRiesgo())) {
            rechazado = true;
            motivosRechazo.append("External Risk HIGH. ");
        }

        // 2. Seniority Policy (Minimum 6 months)
        long mesesAntiguedad = java.time.temporal.ChronoUnit.MONTHS.between(
                solicitud.getAfiliado().getFechaAfiliacion(),
                solicitud.getFechaSolicitud());
        if (mesesAntiguedad < 6) {
            rechazado = true;
            motivosRechazo.append(String.format("Insufficient seniority (%d months, min 6). ", mesesAntiguedad));
        }

        // 3. Max Amount Policy (Max 15 times salary)
        java.math.BigDecimal maxMonto = solicitud.getAfiliado().getSalario().multiply(new java.math.BigDecimal("15"));
        if (solicitud.getMontoSolicitado().compareTo(maxMonto) > 0) {
            rechazado = true;
            motivosRechazo.append("Amount exceeds salary limit. ");
        }

        // 4. Debt Capacity Policy (Installment < 40% Salary)
        java.math.BigDecimal cuotaEstimada = calcularCuota(
                solicitud.getMontoSolicitado(),
                solicitud.getTasaPropuesta(),
                solicitud.getPlazo());
        java.math.BigDecimal capacidadPago = solicitud.getAfiliado().getSalario()
                .multiply(new java.math.BigDecimal("0.40"));

        if (cuotaEstimada.compareTo(capacidadPago) > 0) {
            rechazado = true;
            motivosRechazo.append(String.format("Estimated installment (%s) exceeds payment capacity (%s). ",
                    cuotaEstimada.toPlainString(), capacidadPago.toPlainString()));
        }

        // Final Decision
        if (rechazado) {
            solicitud.setEstado(EstadoSolicitud.RECHAZADO);

            // Build a coherent message explaining the rejection
            String detalleFinal = String.format("Risk Score: %d (%s). REJECTED BY INTERNAL POLICIES: %s",
                    riskResponse.getScore(),
                    riskResponse.getNivelRiesgo(),
                    motivosRechazo.toString());

            solicitud.getEvaluacion().setDetalle(detalleFinal);
        } else {
            solicitud.setEstado(EstadoSolicitud.APROBADO);
            solicitud.getEvaluacion().setDetalle("APPROVED. Risk Score: " + riskResponse.getScore() + " ("
                    + riskResponse.getNivelRiesgo() + "). Meets all internal policies.");
        }
    }

    private java.math.BigDecimal calcularCuota(java.math.BigDecimal monto, java.math.BigDecimal tasaMensualPorcentaje,
            Integer plazo) {
        // Convertir tasa porcentaje a decimal (ej. 1.5% -> 0.015)
        java.math.BigDecimal tasa = tasaMensualPorcentaje.divide(new java.math.BigDecimal("100"), 10,
                java.math.RoundingMode.HALF_UP);

        // Si tasa es 0, división simple
        if (tasa.compareTo(java.math.BigDecimal.ZERO) == 0) {
            return monto.divide(new java.math.BigDecimal(plazo), 2, java.math.RoundingMode.HALF_UP);
        }

        // Fórmula: R = P * [i * (1+i)^n] / [(1+i)^n - 1]
        // (1+i)^n
        java.math.BigDecimal unoMasTasa = java.math.BigDecimal.ONE.add(tasa);
        java.math.BigDecimal factorPotencia = unoMasTasa.pow(plazo);

        // Numerador: P * i * (1+i)^n
        java.math.BigDecimal numerador = monto.multiply(tasa).multiply(factorPotencia);

        // Denominador: (1+i)^n - 1
        java.math.BigDecimal denominador = factorPotencia.subtract(java.math.BigDecimal.ONE);

        return numerador.divide(denominador, 2, java.math.RoundingMode.HALF_UP);
    }
}
