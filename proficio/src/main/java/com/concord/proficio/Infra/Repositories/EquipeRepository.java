package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository
        extends JpaRepository<Equipe, Long> {
}
