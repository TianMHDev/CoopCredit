ALTER TABLE solicitudes
ADD CONSTRAINT fk_solicitud_afiliado
FOREIGN KEY (afiliado_id) REFERENCES afiliados(id);

ALTER TABLE solicitudes
ADD CONSTRAINT fk_solicitud_evaluacion
FOREIGN KEY (evaluacion_id) REFERENCES evaluaciones_riesgo(id);
