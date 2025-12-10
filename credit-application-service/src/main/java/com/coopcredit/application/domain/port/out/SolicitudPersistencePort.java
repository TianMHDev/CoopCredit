package com.coopcredit.application.domain.port.out;

import com.coopcredit.application.domain.model.Solicitud;

import java.util.Optional;

/**
 * Output port for credit application persistence operations.
 * This interface defines the contract for persistence operations
 * without depending on any specific implementation.
 */
public interface SolicitudPersistencePort {

    /**
     * Saves or updates a credit application.
     * 
     * @param solicitud the credit application to save
     * @return the saved credit application with generated ID
     */
    Solicitud save(Solicitud solicitud);

    /**
     * Finds a credit application by its ID.
     * 
     * @param id the credit application ID
     * @return an Optional containing the credit application if found
     */
    Optional<Solicitud> findById(Long id);
}
