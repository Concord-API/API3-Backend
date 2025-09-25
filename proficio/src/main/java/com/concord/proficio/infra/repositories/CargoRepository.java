package com.concord.proficio.Infra.repositories;

import com.concord.proficio.domain.entities.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository
        extends JpaRepository<Colaborador, Long> {
}
