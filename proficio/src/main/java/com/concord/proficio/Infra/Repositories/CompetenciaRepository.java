package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Competencia;
import com.concord.proficio.Domain.Interfaces.ICompetenciaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetenciaRepository
        extends JpaRepository<Competencia, Long>, ICompetenciaRepository {
}
