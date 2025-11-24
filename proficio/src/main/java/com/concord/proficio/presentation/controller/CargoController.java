package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.CargoService;
import com.concord.proficio.domain.entities.Cargo;
import com.concord.proficio.presentation.viewmodel.CargoCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.CargoResponseViewModel;
import com.concord.proficio.presentation.viewmodel.CargoUpdateRequestViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

	private final CargoService cargoService;

	public CargoController(CargoService cargoService) {
		this.cargoService = cargoService;
    }

    @GetMapping
    public ResponseEntity<List<CargoResponseViewModel>> listar(
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "status", required = false, defaultValue = "active") String status,
			@RequestParam(value = "setorId", required = false) Long setorId
    ) {
		List<Cargo> cargos = cargoService.buscar(q, status);
        var vms = cargos.stream()
                .filter(c -> setorId == null || (c.getSetor() != null && setorId.equals(c.getSetor().getId())))
                .map(c -> CargoResponseViewModel.builder()
                        .id_cargo(c.getId())
                        .nome_cargo(c.getNome())
                        .desc_cargo(c.getDescricao())
						.role(c.getRole() != null ? c.getRole().getValue() : null)
                        .status(c.getStatus())
                        .id_setor(c.getSetor() != null ? c.getSetor().getId() : null)
                        .build())
                .toList();
        return ResponseEntity.ok(vms);
    }

	@PostMapping
	public ResponseEntity<CargoResponseViewModel> criar(@Valid @RequestBody CargoCreateRequestViewModel req) {
		Optional<Cargo> existente = cargoService.buscarPorNome(req.getNome());
		if (existente.isPresent()) {
			Cargo c = existente.get();
			if (Boolean.FALSE.equals(c.getStatus())) {
				c.setNome(req.getNome());
				c.setDescricao(req.getDescricao());
				Cargo reativado = cargoService.criar(c, req.getSetorId(), req.getRole());
				var vm = CargoResponseViewModel.builder()
						.id_cargo(reativado.getId())
						.nome_cargo(reativado.getNome())
						.desc_cargo(reativado.getDescricao())
						.role(reativado.getRole() != null ? reativado.getRole().getValue() : null)
						.status(reativado.getStatus())
						.id_setor(reativado.getSetor() != null ? reativado.getSetor().getId() : null)
						.build();
				return ResponseEntity.ok(vm);
			}
			return ResponseEntity.status(409).build();
		}
		Cargo novo = new Cargo();
		novo.setNome(req.getNome());
		novo.setDescricao(req.getDescricao());
		Cargo criado = cargoService.criar(novo, req.getSetorId(), req.getRole());
		var vm = CargoResponseViewModel.builder()
				.id_cargo(criado.getId())
				.nome_cargo(criado.getNome())
				.desc_cargo(criado.getDescricao())
				.role(criado.getRole() != null ? criado.getRole().getValue() : null)
				.status(criado.getStatus())
				.id_setor(criado.getSetor() != null ? criado.getSetor().getId() : null)
				.build();
		return ResponseEntity.status(201).body(vm);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CargoResponseViewModel> atualizar(@PathVariable Long id,
	                                                       @Valid @RequestBody CargoUpdateRequestViewModel req) {
		try {
			return cargoService.atualizar(id, req.getNome(), req.getDescricao(), req.getSetorId(), req.getRole())
					.map(atualizado -> ResponseEntity.ok(CargoResponseViewModel.builder()
							.id_cargo(atualizado.getId())
							.nome_cargo(atualizado.getNome())
							.desc_cargo(atualizado.getDescricao())
							.role(atualizado.getRole() != null ? atualizado.getRole().getValue() : null)
							.status(atualizado.getStatus())
							.id_setor(atualizado.getSetor() != null ? atualizado.getSetor().getId() : null)
							.build()))
					.orElse(ResponseEntity.notFound().build());
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> desativar(@PathVariable Long id) {
		boolean sucesso = cargoService.desativar(id);
		return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}

