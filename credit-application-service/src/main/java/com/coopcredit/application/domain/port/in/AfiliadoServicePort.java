package com.coopcredit.application.domain.port.in;

import com.coopcredit.application.domain.model.Afiliado;

/**
 * Input port (Driving Port) for affiliate management operations.
 * This interface defines the use cases available for affiliate management.
 * It will be implemented by the use case in the application layer.
 */
public interface AfiliadoServicePort {

    /**
     * Registers a new affiliate.
     * Validates document uniqueness and salary constraints.
     * 
     * @param afiliado the affiliate to register
     * @return the registered affiliate with generated ID
     * @throws com.coopcredit.application.domain.exception.AfiliadoAlreadyExistsException if
     *                                                                                    document
     *                                                                                    already
     *                                                                                    exists
     * @throws com.coopcredit.application.domain.exception.InvalidSalaryException         if
     *                                                                                    salary
     *                                                                                    is
     *                                                                                    invalid
     */
    Afiliado registrarAfiliado(Afiliado afiliado);

    /**
     * Updates an existing affiliate.
     * 
     * @param id                the affiliate ID
     * @param datosActualizados the updated affiliate data
     * @return the updated affiliate
     * @throws com.coopcredit.application.domain.exception.AfiliadoNotFoundException if
     *                                                                               affiliate
     *                                                                               not
     *                                                                               found
     * @throws com.coopcredit.application.domain.exception.InvalidSalaryException    if
     *                                                                               salary
     *                                                                               is
     *                                                                               invalid
     */
    Afiliado editarAfiliado(Long id, Afiliado datosActualizados);
}
