package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    Optional<Colaborador> findByEmail(String email);

    long countByEquipeSetorIdAndStatusTrue(Long setorId);

    List<Colaborador> findByEquipeSetorIdAndStatusTrue(Long setorId);
}
