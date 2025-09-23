package com.concord.proficio.Domain.Interfaces;

import com.concord.proficio.Domain.Entities.Equipe;

import java.util.List;
import java.util.Optional;

public interface IEquipeRepository {
    Optional<Equipe> obterPorId(Long idEquipe);
    List<Equipe> listarTodas();
}
