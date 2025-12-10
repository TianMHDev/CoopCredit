package com.coopcredit.application.infrastructure.adapter.in.web.controller;

import com.coopcredit.application.domain.model.Afiliado;
import com.coopcredit.application.domain.port.in.AfiliadoServicePort;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoRegisterRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoResponse;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.AfiliadoUpdateRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.mapper.AfiliadoDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST Controller for affiliate management.
 * This is the driving adapter that exposes HTTP endpoints for affiliate
 * operations.
 */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/afiliados")
@Tag(name = "Afiliados", description = "Affiliate management endpoints")
public class AfiliadoController {

    private final AfiliadoServicePort afiliadoServicePort;
    private final AfiliadoDtoMapper afiliadoDtoMapper;

    public AfiliadoController(AfiliadoServicePort afiliadoServicePort,
            AfiliadoDtoMapper afiliadoDtoMapper) {
        this.afiliadoServicePort = afiliadoServicePort;
        this.afiliadoDtoMapper = afiliadoDtoMapper;
    }

    @PostMapping
    @Operation(summary = "Register a new affiliate", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AfiliadoResponse> registrarAfiliado(
            @Valid @RequestBody AfiliadoRegisterRequest request) {

        // DEBUG: Log the received request
        System.out.println("DEBUG - Received request:");
        System.out.println("  documento: " + request.getDocumento());
        System.out.println("  nombre: " + request.getNombre());
        System.out.println("  salario: " + request.getSalario());
        System.out.println("  estado: " + request.getEstado());

        // Convert DTO to domain model
        Afiliado afiliado = afiliadoDtoMapper.toDomain(request);

        // Execute use case
        Afiliado afiliadoRegistrado = afiliadoServicePort.registrarAfiliado(afiliado);

        // Convert domain model to response DTO
        AfiliadoResponse response = afiliadoDtoMapper.toResponse(afiliadoRegistrado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing affiliate", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<AfiliadoResponse> editarAfiliado(
            @PathVariable Long id,
            @Valid @RequestBody AfiliadoUpdateRequest request) {

        // Convert DTO to domain model
        Afiliado datosActualizados = afiliadoDtoMapper.toDomain(request);

        // Execute use case
        Afiliado afiliadoActualizado = afiliadoServicePort.editarAfiliado(id, datosActualizados);

        // Convert domain model to response DTO
        AfiliadoResponse response = afiliadoDtoMapper.toResponse(afiliadoActualizado);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all affiliates", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<java.util.List<AfiliadoResponse>> listarAfiliados() {
        java.util.List<AfiliadoResponse> response = afiliadoServicePort.listarAfiliados().stream()
                .map(afiliadoDtoMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
