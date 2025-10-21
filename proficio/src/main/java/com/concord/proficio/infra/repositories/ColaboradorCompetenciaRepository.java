package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorCompetenciaRepository extends JpaRepository<ColaboradorCompetencia, Long> {
    List<ColaboradorCompetencia> findByColaboradorId(Long colaboradorId);

    // novo método para encontrar por colaborador + competencia
    Optional<ColaboradorCompetencia> findByColaboradorIdAndCompetenciaId(Long colaboradorId, Long competenciaId);
}