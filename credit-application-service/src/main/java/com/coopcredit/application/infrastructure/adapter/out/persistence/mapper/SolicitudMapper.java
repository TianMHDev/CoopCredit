package com.coopcredit.application.infrastructure.adapter.out.persistence.mapper;

import com.coopcredit.application.domain.model.Solicitud;
import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.SolicitudEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { AfiliadoMapper.class })
public interface SolicitudMapper {

    Solicitud toDomain(SolicitudEntity entity);

    SolicitudEntity toEntity(Solicitud domain);
}
