package com.coopcredit.application.infrastructure.adapter.out.persistence.mapper;

import com.coopcredit.application.domain.model.Afiliado;
import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.AfiliadoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between Afiliado (domain) and AfiliadoEntity
 * (persistence).
 * This mapper is automatically implemented by MapStruct at compile time.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AfiliadoMapper {

    /**
     * Converts domain model to entity.
     * 
     * @param afiliado the domain model
     * @return the entity
     */
    AfiliadoEntity toEntity(Afiliado afiliado);

    /**
     * Converts entity to domain model.
     * 
     * @param entity the entity
     * @return the domain model
     */
    Afiliado toDomain(AfiliadoEntity entity);
}
