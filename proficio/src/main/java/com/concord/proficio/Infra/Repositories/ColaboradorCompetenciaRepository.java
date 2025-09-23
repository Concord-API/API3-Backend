package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.ColaboradorCompetencia;
import com.concord.proficio.Domain.Interfaces.IColaboradorCompetenciaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorCompetenciaRepository
        extends JpaRepository<ColaboradorCompetencia, Long>, IColaboradorCompetenciaRepository {
}
