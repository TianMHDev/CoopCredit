package com.coopcredit.application.infrastructure.adapter.out.persistence;

import com.coopcredit.application.domain.model.Afiliado;
import com.coopcredit.application.domain.port.out.AfiliadoPersistencePort;
import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.AfiliadoEntity;
import com.coopcredit.application.infrastructure.adapter.out.persistence.mapper.AfiliadoMapper;
import com.coopcredit.application.infrastructure.adapter.out.persistence.repository.AfiliadoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Persistence adapter implementing the AfiliadoPersistencePort.
 * This adapter bridges the domain layer with the infrastructure layer.
 * It uses the JPA repository and MapStruct mapper to persist and retrieve
 * affiliates.
 */
@Component
public class AfiliadoPersistenceAdapter implements AfiliadoPersistencePort {

    private final AfiliadoJpaRepository afiliadoJpaRepository;
    private final AfiliadoMapper afiliadoMapper;

    /**
     * Constructor with dependency injection.
     * 
     * @param afiliadoJpaRepository the JPA repository
     * @param afiliadoMapper        the MapStruct mapper
     */
    public AfiliadoPersistenceAdapter(AfiliadoJpaRepository afiliadoJpaRepository,
            AfiliadoMapper afiliadoMapper) {
        this.afiliadoJpaRepository = afiliadoJpaRepository;
        this.afiliadoMapper = afiliadoMapper;
    }

    /**
     * Saves an affiliate to the database.
     * Converts domain model to entity, saves it, and converts back to domain model.
     * 
     * @param afiliado the affiliate domain model
     * @return the saved affiliate with generated ID
     */
    @Override
    public Afiliado save(Afiliado afiliado) {
        AfiliadoEntity entity = afiliadoMapper.toEntity(afiliado);
        AfiliadoEntity savedEntity = afiliadoJpaRepository.save(entity);
        return afiliadoMapper.toDomain(savedEntity);
    }

    /**
     * Finds an affiliate by document number.
     * 
     * @param documento the document number
     * @return Optional containing the affiliate if found
     */
    @Override
    public Optional<Afiliado> findByDocumento(String documento) {
        return afiliadoJpaRepository.findByDocumento(documento)
                .map(afiliadoMapper::toDomain);
    }

    /**
     * Finds an affiliate by ID.
     * 
     * @param id the affiliate ID
     * @return Optional containing the affiliate if found
     */
    @Override
    public Optional<Afiliado> findById(Long id) {
        return afiliadoJpaRepository.findById(id)
                .map(afiliadoMapper::toDomain);
    }

    @Override
    public java.util.List<Afiliado> findAll() {
        return afiliadoJpaRepository.findAll().stream()
                .map(afiliadoMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}
