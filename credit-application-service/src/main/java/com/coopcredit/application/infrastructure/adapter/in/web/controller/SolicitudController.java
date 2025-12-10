package com.coopcredit.application.infrastructure.adapter.in.web.controller;

import com.coopcredit.application.domain.model.Solicitud;
import com.coopcredit.application.domain.port.in.SolicitudServicePort;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.SolicitudRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.SolicitudResponse;
import com.coopcredit.application.infrastructure.adapter.in.web.mapper.SolicitudDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudServicePort solicitudServicePort;
    private final SolicitudDtoMapper solicitudDtoMapper;

    @PostMapping
    public ResponseEntity<SolicitudResponse> registrarSolicitud(
            @Valid @RequestBody SolicitudRequest request) {

        // Convert DTO to domain
        Solicitud solicitud = solicitudDtoMapper.toDomain(request);

        // Execute use case
        Solicitud solicitudRegistrada = solicitudServicePort.registrarSolicitud(solicitud);

        // Convert domain to response DTO
        SolicitudResponse response = solicitudDtoMapper.toResponse(solicitudRegistrada);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
