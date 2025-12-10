package com.coopcredit.application.infrastructure.adapter.out.persistence.repository;

import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.AfiliadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for AfiliadoEntity.
 * Provides database access methods for affiliate entities.
 */
@Repository
public interface AfiliadoJpaRepository extends JpaRepository<AfiliadoEntity, Long> {

    /**
     * Finds an affiliate by document number.
     * 
     * @param documento the document number
     * @return Optional containing the affiliate entity if found
     */
    Optional<AfiliadoEntity> findByDocumento(String documento);
}
