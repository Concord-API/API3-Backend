package com.concord.proficio.Infra.repositories;

import com.concord.proficio.domain.entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository
        extends JpaRepository<Equipe, Long> {
}
