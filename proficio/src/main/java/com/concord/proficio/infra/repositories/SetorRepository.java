package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SetorRepository extends JpaRepository<Setor, Long> {
    Optional<Setor> findByNome(String nome);
}
