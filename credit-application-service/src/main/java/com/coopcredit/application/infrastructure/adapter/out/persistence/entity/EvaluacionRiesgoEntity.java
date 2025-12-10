package com.coopcredit.application.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones_riesgo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionRiesgoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "nivel_riesgo", nullable = false)
    private String nivelRiesgo;

    @Column(nullable = false)
    private String detalle;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDateTime fechaEvaluacion;
}
