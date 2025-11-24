package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Squad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SquadRepository extends JpaRepository<Squad, Long> {
    Optional<Squad> findByNome(String nome);
    List<Squad> findByStatusTrue();
}





