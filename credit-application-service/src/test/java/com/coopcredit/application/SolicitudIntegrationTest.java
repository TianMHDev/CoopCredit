package com.coopcredit.application;

import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoRegisterRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.SolicitudRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.LoginRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Set;

import com.coopcredit.application.domain.model.EvaluacionRiesgoResponse;
import com.coopcredit.application.domain.port.out.RiskCentralPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitudIntegrationTest {

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
                registry.add("spring.datasource.username", () -> "sa");
                registry.add("spring.datasource.password", () -> "");
                registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
                registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
                registry.add("spring.flyway.enabled", () -> "true");
                registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        }

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private RiskCentralPort riskCentralPort;

        private String token;
        private final String USERNAME = "afiliado_test_int";

        @BeforeEach
        void setUp() throws Exception {
                // 1. Register User (Auth)
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setUsername(USERNAME);
                registerRequest.setPassword("password123");
                registerRequest.setRole(Set.of("ROLE_AFILIADO"));

                mockMvc.perform(post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)));
                // No check status because it might exist from previous run if container reused
                // (unlikely with @Container static but possible)

                // 2. Login to get Token
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(USERNAME);
                loginRequest.setPassword("password123");

                MvcResult result = mockMvc.perform(post("/api/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andReturn();

                String response = result.getResponse().getContentAsString();
                token = objectMapper.readTree(response).get("token").asText();

                // 3. Register Affiliate (Domain)
                // Check if exists first or just try to register and ignore conflict
                AfiliadoRegisterRequest afiliadoRequest = new AfiliadoRegisterRequest();
                afiliadoRequest.setDocumento(USERNAME);
                afiliadoRequest.setNombre("Test Affiliate Integration");
                afiliadoRequest.setSalario(new BigDecimal("5000.00"));
                afiliadoRequest.setEstado(com.coopcredit.application.domain.model.EstadoAfiliado.ACTIVO);

                mockMvc.perform(post("/api/v1/afiliados")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(afiliadoRequest)));
        }

        @Test
        void registrarSolicitud_ShouldSucceed_WhenAuthenticatedAndValid() throws Exception {
                // Mock External Service
                EvaluacionRiesgoResponse mockResponse = new EvaluacionRiesgoResponse();
                mockResponse.setScore(850);
                mockResponse.setNivelRiesgo("BAJO");
                mockResponse.setDetalle("Mocked response");

                when(riskCentralPort.evaluateRisk(anyString(), any(), anyInt())).thenReturn(mockResponse);

                SolicitudRequest solicitudRequest = new SolicitudRequest();
                solicitudRequest.setDocumentoAfiliado(USERNAME);
                solicitudRequest.setMontoSolicitado(new BigDecimal("1000.00"));
                solicitudRequest.setPlazo(12);
                solicitudRequest.setTasaPropuesta(new BigDecimal("2.0"));

                mockMvc.perform(post("/api/v1/solicitudes")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(solicitudRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.estado").exists())
                                .andExpect(jsonPath("$.evaluacion").exists());
        }

        @Test
        void registrarSolicitud_ShouldFail_WhenUnauthorizedUser() throws Exception {
                SolicitudRequest solicitudRequest = new SolicitudRequest();
                solicitudRequest.setDocumentoAfiliado("OTRO_USUARIO"); // Trying to register for someone else
                solicitudRequest.setMontoSolicitado(new BigDecimal("1000.00"));
                solicitudRequest.setPlazo(12);
                solicitudRequest.setTasaPropuesta(new BigDecimal("2.0"));

                mockMvc.perform(post("/api/v1/solicitudes")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(solicitudRequest)))
                                .andExpect(status().isForbidden()); // Or 500/403 depending on handler
        }
}
