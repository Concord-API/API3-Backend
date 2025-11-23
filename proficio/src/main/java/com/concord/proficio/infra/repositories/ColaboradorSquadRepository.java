package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.ColaboradorSquad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColaboradorSquadRepository extends JpaRepository<ColaboradorSquad, Long> {
    List<ColaboradorSquad> findByColaboradorIdAndStatusTrue(Long colaboradorId);
    List<ColaboradorSquad> findBySquadIdAndStatusTrue(Long squadId);
    long countBySquadIdAndStatusTrue(Long squadId);
    Optional<ColaboradorSquad> findByColaboradorIdAndSquadId(Long colaboradorId, Long squadId);
}





