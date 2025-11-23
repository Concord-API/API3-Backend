package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNome(String nome);

    List<Cargo> findByStatusTrue();
}
