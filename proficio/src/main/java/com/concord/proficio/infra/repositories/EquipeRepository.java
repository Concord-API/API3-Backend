package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.domain.entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository
        extends JpaRepository<Equipe, Long> {
    Optional<Equipe> findByNome(String nome);

    long countBySetorIdAndStatusTrue(Long setorId);

    List<Equipe> findBySetorIdAndStatusTrue(Long setorId);

    List<Equipe> findByStatusTrue();

    @Query("select e from Setor e where e.status = true and (:q is null or lower(s.nome) like lower(concat('%', :q, '%')))")
    List<Equipe> searchActive(@Param("q") String q);

}
