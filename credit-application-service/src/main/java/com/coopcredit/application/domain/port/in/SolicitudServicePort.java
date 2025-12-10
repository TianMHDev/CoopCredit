package com.coopcredit.application.domain.port.in;

import com.coopcredit.application.domain.model.Solicitud;

/**
 * Input port for credit application management use cases.
 */
public interface SolicitudServicePort {

    /**
     * Registers a new credit application and initiates the evaluation process.
     * 
     * @param solicitud the credit application to register
     * @return the registered credit application with evaluation result
     */
    Solicitud registrarSolicitud(Solicitud solicitud);
}
