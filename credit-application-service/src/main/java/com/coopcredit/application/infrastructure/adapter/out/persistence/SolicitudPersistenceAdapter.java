package com.coopcredit.application.infrastructure.adapter.out.persistence;

import com.coopcredit.application.domain.model.Solicitud;
import com.coopcredit.application.domain.port.out.SolicitudPersistencePort;
import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.SolicitudEntity;
import com.coopcredit.application.infrastructure.adapter.out.persistence.mapper.SolicitudMapper;
import com.coopcredit.application.infrastructure.adapter.out.persistence.repository.SolicitudJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SolicitudPersistenceAdapter implements SolicitudPersistencePort {

    private final SolicitudJpaRepository solicitudJpaRepository;
    private final SolicitudMapper solicitudMapper;

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = solicitudMapper.toEntity(solicitud);
        SolicitudEntity savedEntity = solicitudJpaRepository.save(entity);
        return solicitudMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        java.util.Optional<SolicitudEntity> entityOpt = solicitudJpaRepository.findById(id);

        if (entityOpt.isEmpty()) {
            return java.util.Optional.empty();
        }

        SolicitudEntity entity = entityOpt.get();

        // Seguridad: Validar propiedad si es ROLE_AFILIADO
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_AFILIADO"))) {
            String username = auth.getName();
            if (!username.equals(entity.getAfiliado().getDocumento())) {
                return java.util.Optional.empty();
            }
        }

        return java.util.Optional.of(solicitudMapper.toDomain(entity));
    }
}
