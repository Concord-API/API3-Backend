package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.SquadService;
import com.concord.proficio.domain.entities.Squad;
import com.concord.proficio.presentation.viewmodel.SquadCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.SquadMemberAddRequestViewModel;
import com.concord.proficio.presentation.viewmodel.SquadResponseViewModel;
import com.concord.proficio.presentation.viewmodel.SquadUpdateRequestViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/squads")
public class SquadController {

	private final SquadService squadService;

	public SquadController(SquadService squadService) {
		this.squadService = squadService;
	}

	@GetMapping
	public ResponseEntity<List<SquadResponseViewModel>> listar(
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "status", required = false, defaultValue = "active") String status
	) {
		var vms = squadService.listarDTO(q, status).stream()
				.map(dto -> SquadResponseViewModel.builder()
						.id(dto.getId())
						.nome(dto.getNome())
						.descricao(dto.getDescricao())
						.status(dto.getStatus())
						.liderId(dto.getLiderId())
						.membrosCount(dto.getMembrosCount())
						.build())
				.toList();
		return ResponseEntity.ok(vms);
	}

	@PostMapping
	public ResponseEntity<SquadResponseViewModel> criar(@Valid @RequestBody SquadCreateRequestViewModel req) {
		Optional<Squad> existente = squadService.buscarPorNome(req.getNome());
		if (existente.isPresent()) {
			Squad e = existente.get();
			if (Boolean.FALSE.equals(e.getStatus())) {
				e.setDescricao(req.getDescricao());
				Squad reativado = squadService.criar(e, req.getLiderId());
				var vm = SquadResponseViewModel.builder()
						.id(reativado.getId())
						.nome(reativado.getNome())
						.descricao(reativado.getDescricao())
						.status(reativado.getStatus())
						.membrosCount(squadService.contarMembrosAtivos(reativado.getId()))
						.build();
				return ResponseEntity.ok(vm);
			}
			return ResponseEntity.status(409).build();
		}
		Squad novo = new Squad(null, req.getNome(), req.getDescricao(), true, null);
		Squad criado = squadService.criar(novo, req.getLiderId());
		var vm = SquadResponseViewModel.builder()
				.id(criado.getId())
				.nome(criado.getNome())
				.descricao(criado.getDescricao())
				.status(criado.getStatus())
				.liderId(criado.getLider() != null ? criado.getLider().getId() : null)
				.membrosCount(squadService.contarMembrosAtivos(criado.getId()))
				.build();
		return ResponseEntity.status(201).body(vm);
	}

	@PutMapping("/{id}")
	public ResponseEntity<SquadResponseViewModel> atualizar(@PathVariable Long id, @Valid @RequestBody SquadUpdateRequestViewModel req) {
		return squadService.atualizar(id, req.getNome(), req.getDescricao(), req.getLiderId())
				.map(s -> ResponseEntity.ok(SquadResponseViewModel.builder()
						.id(s.getId())
						.nome(s.getNome())
						.descricao(s.getDescricao())
						.status(s.getStatus())
						.liderId(s.getLider() != null ? s.getLider().getId() : null)
						.membrosCount(squadService.contarMembrosAtivos(s.getId()))
						.build()))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> desativar(@PathVariable Long id) {
		boolean ok = squadService.desativar(id);
		return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}/colaboradores")
	public ResponseEntity<List<Long>> listarMembros(@PathVariable Long id) {
		return squadService.buscarPorId(id)
				.map(s -> ResponseEntity.ok(
						squadService.listarMembrosAtivos(id).stream()
								.map(c -> c.getId())
								.toList()
				))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/{id}/colaboradores")
	public ResponseEntity<Void> adicionarMembro(@PathVariable Long id, @Valid @RequestBody SquadMemberAddRequestViewModel req) {
		boolean ok = squadService.adicionarMembro(id, req.getColaboradorId());
		return ok ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
	}

	@DeleteMapping("/{id}/colaboradores/{colaboradorId}")
	public ResponseEntity<Void> removerMembro(@PathVariable Long id, @PathVariable Long colaboradorId) {
		boolean ok = squadService.removerMembro(id, colaboradorId);
		return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}


