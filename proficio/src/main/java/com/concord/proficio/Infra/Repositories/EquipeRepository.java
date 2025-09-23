package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Equipe;
import com.concord.proficio.Domain.Interfaces.IEquipeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipeRepository
        extends JpaRepository<Equipe, Long>, IEquipeRepository {
}
