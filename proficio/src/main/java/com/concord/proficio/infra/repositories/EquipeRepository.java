package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipeRepository
        extends JpaRepository<Equipe, Long> {
    Optional<Equipe> findByNome(String nome);
}
