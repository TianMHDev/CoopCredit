package com.coopcredit.application.infrastructure.config;

import com.coopcredit.application.application.usecase.GestionAfiliadosUseCase;
import com.coopcredit.application.domain.port.in.AfiliadoServicePort;
import com.coopcredit.application.domain.port.out.AfiliadoPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for dependency injection of use cases.
 * This configuration wires the hexagonal architecture components together.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates and configures the AfiliadoServicePort bean.
     * This bean is the implementation of the use case.
     * 
     * @param afiliadoPersistencePort the persistence port implementation
     * @return the configured service port
     */
    @Bean
    public AfiliadoServicePort afiliadoServicePort(AfiliadoPersistencePort afiliadoPersistencePort) {
        return new GestionAfiliadosUseCase(afiliadoPersistencePort);
    }
}
