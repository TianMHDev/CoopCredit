package com.coopcredit.application.application.usecase;

import com.coopcredit.application.domain.exception.AfiliadoAlreadyExistsException;
import com.coopcredit.application.domain.exception.AfiliadoNotFoundException;
import com.coopcredit.application.domain.exception.InvalidSalaryException;
import com.coopcredit.application.domain.model.Afiliado;
import com.coopcredit.application.domain.model.EstadoAfiliado;
import com.coopcredit.application.domain.port.in.AfiliadoServicePort;
import com.coopcredit.application.domain.port.out.AfiliadoPersistencePort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Use case implementation for affiliate management.
 * This class contains the business logic for registering and editing
 * affiliates.
 * It depends only on the port interfaces, not on specific implementations.
 */
public class GestionAfiliadosUseCase implements AfiliadoServicePort {

    private final AfiliadoPersistencePort afiliadoPersistencePort;

    /**
     * Constructor with dependency injection of the persistence port.
     * 
     * @param afiliadoPersistencePort the persistence port implementation
     */
    public GestionAfiliadosUseCase(AfiliadoPersistencePort afiliadoPersistencePort) {
        this.afiliadoPersistencePort = afiliadoPersistencePort;
    }

    /**
     * Registers a new affiliate with business validations.
     * 
     * Validation 1: Document uniqueness - ensures no duplicate documents exist
     * Validation 2: Salary validation - ensures salary is greater than zero
     * 
     * @param afiliado the affiliate to register
     * @return the registered affiliate with generated ID
     * @throws AfiliadoAlreadyExistsException if document already exists
     * @throws InvalidSalaryException         if salary is not greater than zero
     */
    @Override
    public Afiliado registrarAfiliado(Afiliado afiliado) {
        // Validation 1: Check document uniqueness
        Optional<Afiliado> existingAfiliado = afiliadoPersistencePort.findByDocumento(afiliado.getDocumento());
        if (existingAfiliado.isPresent()) {
            throw new AfiliadoAlreadyExistsException(afiliado.getDocumento());
        }

        // Validation 2: Validate salary is greater than zero
        if (afiliado.getSalario() == null || afiliado.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSalaryException(afiliado.getSalario());
        }

        // Set initial state if not provided
        if (afiliado.getEstado() == null) {
            afiliado.setEstado(EstadoAfiliado.ACTIVO);
        }

        // Set affiliation date if not provided
        if (afiliado.getFechaAfiliacion() == null) {
            afiliado.setFechaAfiliacion(LocalDate.now());
        }

        // Save and return the affiliate
        return afiliadoPersistencePort.save(afiliado);
    }

    /**
     * Updates an existing affiliate with business validations.
     * 
     * @param id                the affiliate ID
     * @param datosActualizados the updated affiliate data
     * @return the updated affiliate
     * @throws AfiliadoNotFoundException if affiliate with given ID is not found
     * @throws InvalidSalaryException    if salary is not greater than zero
     */
    @Override
    public Afiliado editarAfiliado(Long id, Afiliado datosActualizados) {
        // Find existing affiliate
        Afiliado afiliadoExistente = afiliadoPersistencePort.findById(id)
                .orElseThrow(() -> new AfiliadoNotFoundException(id));

        // Validation: Validate salary if it's being updated
        if (datosActualizados.getSalario() != null) {
            if (datosActualizados.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSalaryException(datosActualizados.getSalario());
            }
            afiliadoExistente.setSalario(datosActualizados.getSalario());
        }

        // Update basic information
        if (datosActualizados.getNombre() != null) {
            afiliadoExistente.setNombre(datosActualizados.getNombre());
        }

        if (datosActualizados.getEstado() != null) {
            afiliadoExistente.setEstado(datosActualizados.getEstado());
        }

        // Note: Document number should not be updated as it's a unique identifier
        // Note: Affiliation date should not be changed after registration

        // Save and return the updated affiliate
        return afiliadoPersistencePort.save(afiliadoExistente);
    }
}
