package com.concord.proficio.Infra.Repositories;

import com.concord.proficio.Domain.Entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Long> {
}
