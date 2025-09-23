package com.concord.proficio.Domain.Interfaces;

import com.concord.proficio.Domain.Entities.Cargo;

import java.util.List;
import java.util.Optional;

public interface ICargoRepository {
    Optional<Cargo> obterPorId(Long idCargo);
}
