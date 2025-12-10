package com.coopcredit.application.infrastructure.adapter.out.persistence.entity;

import com.coopcredit.application.domain.model.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "afiliado_id", nullable = false)
    private AfiliadoEntity afiliado;

    @Column(name = "monto_solicitado", nullable = false)
    private BigDecimal montoSolicitado;

    @Column(nullable = false)
    private Integer plazo;

    @Column(name = "tasa_propuesta", nullable = false)
    private BigDecimal tasaPropuesta;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evaluacion_id", referencedColumnName = "id")
    private EvaluacionRiesgoEntity evaluacion;
}
