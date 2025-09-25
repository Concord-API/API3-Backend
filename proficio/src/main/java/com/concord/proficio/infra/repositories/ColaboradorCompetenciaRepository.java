package com.concord.proficio.Infra.repositories;

import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorCompetenciaRepository
        extends JpaRepository<ColaboradorCompetencia, Long> {
}
