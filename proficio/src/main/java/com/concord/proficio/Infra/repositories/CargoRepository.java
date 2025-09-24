package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository
        extends JpaRepository<Colaborador, Long> {
}
