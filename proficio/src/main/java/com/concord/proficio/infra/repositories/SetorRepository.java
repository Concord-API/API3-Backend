package com.concord.proficio.infra.repositories;

import com.concord.proficio.domain.entities.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SetorRepository extends JpaRepository<Setor, Long> {
    Optional<Setor> findByNome(String nome);

    List<Setor> findByStatusTrue();

    @Query("select s from Setor s where s.status = true and (:q is null or lower(s.nome) like lower(concat('%', :q, '%')))")
    List<Setor> searchActive(@Param("q") String q);
}
