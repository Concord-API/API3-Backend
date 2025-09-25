package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetenciaRepository
        extends JpaRepository<Competencia, Long> {
}
