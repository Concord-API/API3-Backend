package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {
    java.util.List<Competencia> findByStatusTrue();
}
