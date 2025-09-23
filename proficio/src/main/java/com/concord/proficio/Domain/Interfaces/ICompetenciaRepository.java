package com.concord.proficio.Domain.Interfaces;

import com.concord.proficio.Domain.Entities.Competencia;

import java.util.List;
import java.util.Optional;

public interface ICompetenciaRepository {
    List<Competencia> listarTodas();
    Competencia criar(Competencia competencia);
    Optional<Competencia> encontrarPorNome(String nome);
    boolean existsPorNome(String nome);
    Optional<Competencia> obterPorId(Long idCompetencia);
}
