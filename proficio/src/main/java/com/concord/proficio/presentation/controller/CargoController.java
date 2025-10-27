package com.concord.proficio.presentation.controller;

import com.concord.proficio.domain.entities.Cargo;
import com.concord.proficio.infra.repositories.CargoRepository;
import com.concord.proficio.presentation.viewmodel.CargoResponseViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final CargoRepository cargoRepository;

    public CargoController(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @GetMapping
    public ResponseEntity<List<CargoResponseViewModel>> listar(
            @RequestParam(value = "setorId", required = false) Long setorId,
            @RequestParam(value = "status", required = false) Boolean status
    ) {
        List<Cargo> cargos = cargoRepository.findAll();
        var vms = cargos.stream()
                .filter(c -> setorId == null || (c.getSetor() != null && setorId.equals(c.getSetor().getId())))
                .filter(c -> status == null || status.equals(c.getStatus()))
                .map(c -> CargoResponseViewModel.builder()
                        .id_cargo(c.getId())
                        .nome_cargo(c.getNome())
                        .desc_cargo(c.getDescricao())
                        .status(c.getStatus())
                        .id_setor(c.getSetor() != null ? c.getSetor().getId() : null)
                        .build())
                .toList();
        return ResponseEntity.ok(vms);
    }
}


