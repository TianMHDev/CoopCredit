package com.coopcredit.application.infrastructure.adapter.out.persistence.repository;

import com.coopcredit.application.infrastructure.adapter.out.persistence.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudJpaRepository extends JpaRepository<SolicitudEntity, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = { "afiliado", "evaluacion" })
    @org.springframework.data.jpa.repository.Query("SELECT s FROM SolicitudEntity s")
    java.util.List<SolicitudEntity> findAllWithRelations();
}
