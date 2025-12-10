package com.coopcredit.application.domain.port.out;

import com.coopcredit.application.domain.model.Afiliado;

import java.util.Optional;

/**
 * Output port (Driven Port) for affiliate persistence operations.
 * This interface defines the contract for persisting and retrieving affiliates.
 * It will be implemented by the persistence adapter in the infrastructure
 * layer.
 */
public interface AfiliadoPersistencePort {

    /**
     * Saves an affiliate (create or update).
     * 
     * @param afiliado the affiliate to save
     * @return the saved affiliate with generated ID if new
     */
    Afiliado save(Afiliado afiliado);

    /**
     * Finds an affiliate by document number.
     * Used for uniqueness validation.
     * 
     * @param documento the document number
     * @return Optional containing the affiliate if found, empty otherwise
     */
    Optional<Afiliado> findByDocumento(String documento);

    /**
     * Finds an affiliate by ID.
     * Used for retrieval and updates.
     * 
     * @param id the affiliate ID
     * @return Optional containing the affiliate if found, empty otherwise
     */
    Optional<Afiliado> findById(Long id);
}
