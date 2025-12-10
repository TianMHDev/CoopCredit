package com.coopcredit.application.infrastructure.adapter.in.web.mapper;

import com.coopcredit.application.domain.model.Afiliado;
import com.coopcredit.application.domain.model.Solicitud;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.SolicitudRequest;
import com.coopcredit.application.infrastructure.adapter.in.web.dto.SolicitudResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitudDtoMapper {

    @Mapping(target = "afiliado", source = "documentoAfiliado", qualifiedByName = "documentoToAfiliado")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaSolicitud", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "evaluacion", ignore = true)
    Solicitud toDomain(SolicitudRequest request);

    @Mapping(target = "documentoAfiliado", source = "afiliado.documento")
    @Mapping(target = "nombreAfiliado", source = "afiliado.nombre")
    SolicitudResponse toResponse(Solicitud domain);

    @Named("documentoToAfiliado")
    default Afiliado documentoToAfiliado(String documento) {
        if (documento == null) {
            return null;
        }
        Afiliado afiliado = new Afiliado();
        afiliado.setDocumento(documento);
        return afiliado;
    }
}
