package com.concord.proficio.Infra.repositories;

import com.concord.proficio.domain.entities.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetenciaRepository
        extends JpaRepository<Competencia, Long> {
}
