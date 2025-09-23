package com.concord.proficio.Domain.Interfaces;

import com.concord.proficio.Domain.Entities.Colaborador;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IColaboradorRepository {
    Optional<Colaborador> buscarPorUsuarioId(Long colaboradorId);
    void atualizarFoto(Long colaboradorId, byte[] avatar);
    void tocarAtualizadoEm(Long colaboradorId, LocalDateTime data);
}
