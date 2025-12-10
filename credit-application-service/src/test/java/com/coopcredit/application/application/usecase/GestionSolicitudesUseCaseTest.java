package com.coopcredit.application.application.usecase;

import com.coopcredit.application.domain.model.*;
import com.coopcredit.application.domain.port.out.AfiliadoPersistencePort;
import com.coopcredit.application.domain.port.out.RiskCentralPort;
import com.coopcredit.application.domain.port.out.SolicitudPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionSolicitudesUseCaseTest {

    @Mock
    private AfiliadoPersistencePort afiliadoPersistencePort;

    @Mock
    private SolicitudPersistencePort solicitudPersistencePort;

    @Mock
    private RiskCentralPort riskCentralPort;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private GestionSolicitudesUseCase gestionSolicitudesUseCase;

    private Afiliado afiliado;
    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        afiliado = new Afiliado();
        afiliado.setId(1L);
        afiliado.setDocumento("12345678");
        afiliado.setEstado(EstadoAfiliado.ACTIVO);
        afiliado.setSalario(new BigDecimal("5000.00"));
        afiliado.setFechaAfiliacion(LocalDate.now().minusMonths(12)); // 1 año de antigüedad

        solicitud = new Solicitud();
        solicitud.setAfiliado(afiliado);
        solicitud.setMontoSolicitado(new BigDecimal("1000.00"));
        solicitud.setPlazo(12);
        solicitud.setTasaPropuesta(new BigDecimal("2.0"));

        // Mock Security Context
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void registrarSolicitud_Aprobada_CuandoCumplePoliticas() {
        // Arrange
        when(afiliadoPersistencePort.findByDocumento("12345678")).thenReturn(Optional.of(afiliado));
        when(solicitudPersistencePort.save(any(Solicitud.class))).thenAnswer(i -> i.getArguments()[0]);

        EvaluacionRiesgoResponse riskResponse = new EvaluacionRiesgoResponse();
        riskResponse.setScore(800);
        riskResponse.setNivelRiesgo("BAJO");
        riskResponse.setDetalle("Buen comportamiento");

        when(riskCentralPort.evaluateRisk("12345678", new BigDecimal("1000.00"), 12))
                .thenReturn(riskResponse);

        // Act
        Solicitud resultado = gestionSolicitudesUseCase.registrarSolicitud(solicitud);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoSolicitud.APROBADO, resultado.getEstado());
        assertNotNull(resultado.getEvaluacion());
        assertEquals(800, resultado.getEvaluacion().getScore());

        verify(solicitudPersistencePort, times(2)).save(any(Solicitud.class)); // Inicial y Final
    }

    @Test
    void registrarSolicitud_Rechazada_CuandoRiesgoAlto() {
        // Arrange
        when(afiliadoPersistencePort.findByDocumento("12345678")).thenReturn(Optional.of(afiliado));
        when(solicitudPersistencePort.save(any(Solicitud.class))).thenAnswer(i -> i.getArguments()[0]);

        EvaluacionRiesgoResponse riskResponse = new EvaluacionRiesgoResponse();
        riskResponse.setScore(300);
        riskResponse.setNivelRiesgo("ALTO");
        riskResponse.setDetalle("Mal comportamiento");

        when(riskCentralPort.evaluateRisk("12345678", new BigDecimal("1000.00"), 12))
                .thenReturn(riskResponse);

        // Act
        Solicitud resultado = gestionSolicitudesUseCase.registrarSolicitud(solicitud);

        // Assert
        assertEquals(EstadoSolicitud.RECHAZADO, resultado.getEstado());
        assertTrue(resultado.getEvaluacion().getDetalle().contains("External Risk HIGH"));
    }
}
