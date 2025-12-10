package com.coopcredit.application;

import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoRegisterRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoUpdateRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AfiliadoIntegrationTest {

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

    private String token;
    private Long afiliadoId;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Register User (Auth)
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("admin_test");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Set.of("ROLE_ADMIN"));

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // 2. Login to get Token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin_test");
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        token = objectMapper.readTree(response).get("token").asText();

        // 3. Register Affiliate
        AfiliadoRegisterRequest afiliadoRequest = new AfiliadoRegisterRequest();
        afiliadoRequest.setDocumento("99999999");
        afiliadoRequest.setNombre("Original Name");
        afiliadoRequest.setSalario(new BigDecimal("5000.00"));
        afiliadoRequest.setEstado(com.coopcredit.application.domain.model.EstadoAfiliado.ACTIVO);

        MvcResult createResult = mockMvc.perform(post("/api/v1/afiliados")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(afiliadoRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        afiliadoId = objectMapper.readTree(createResponse).get("id").asLong();
    }

    @Test
    void updateAfiliado_ShouldUpdateMultipleFields() throws Exception {
        AfiliadoUpdateRequest updateRequest = new AfiliadoUpdateRequest();
        updateRequest.setNombre("Updated Name");
        updateRequest.setSalario(new BigDecimal("7000.00"));

        mockMvc.perform(put("/api/v1/afiliados/" + afiliadoId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Updated Name"))
                .andExpect(jsonPath("$.salario").value(7000.00));
    }

    @Test
    void listAfiliados_ShouldReturnList() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/afiliados")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].documento").exists());
    }
}
