package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorCompetenciaRepository extends JpaRepository<ColaboradorCompetencia, Long> {
    List<ColaboradorCompetencia> findByColaboradorId(Long colaboradorId);
}
