package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.AvaliacaoColaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoColaboradorRepository extends JpaRepository<AvaliacaoColaborador, Long> {
    List<AvaliacaoColaborador> findByAvaliadoId(Long avaliadoId);
    List<AvaliacaoColaborador> findByAvaliadorId(Long avaliadorId);
    List<AvaliacaoColaborador> findByAvaliadoIdAndStatusTrue(Long avaliadoId);
    List<AvaliacaoColaborador> findByAvaliadorIdAndStatusTrue(Long avaliadorId);
    Optional<AvaliacaoColaborador> findByIdAndStatusTrue(Long id);
}

