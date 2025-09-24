package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorRepository
        extends JpaRepository<Colaborador, Long> {
}
