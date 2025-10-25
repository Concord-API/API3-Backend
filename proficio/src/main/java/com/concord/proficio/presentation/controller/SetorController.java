package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.SetorService;
import com.concord.proficio.domain.entities.Setor;
import com.concord.proficio.presentation.viewmodel.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/setores")
public class SetorController {

	private final SetorService setorService;

	public SetorController(SetorService setorService) {
		this.setorService = setorService;
	}

	@GetMapping
    public ResponseEntity<List<SetorResponseViewModel>> listar(
                @RequestParam(value = "q", required = false) String q) {
        List<SetorResponseViewModel> vms = setorService.buscar(q).stream()
				.map(s -> SetorResponseViewModel.builder()
						.id(s.getId())
						.nome(s.getNome())
						.descricao(s.getDescricao())
						.status(s.getStatus())
						.diretorId(s.getDiretor() != null ? s.getDiretor().getId() : null)
						.equipesCount(setorService.contarEquipesAtivasDoSetor(s.getId()))
						.colaboradoresCount(setorService.contarColaboradoresAtivosDoSetor(s.getId()))
						.build())
				.collect(Collectors.toList());
		return ResponseEntity.ok(vms);
	}

	@PostMapping
	public ResponseEntity<SetorResponseViewModel> criar(@Valid @RequestBody SetorCreateRequestViewModel req) {
		return setorService.buscarPorNome(req.getNome())
				.map(existing -> {
					if (Boolean.FALSE.equals(existing.getStatus())) {
						existing.setStatus(true);
						existing.setDescricao(req.getDescricao());
						Setor reativado = setorService.criar(existing, req.getDiretorId());
						SetorResponseViewModel vm = SetorResponseViewModel.builder()
								.id(reativado.getId())
								.nome(reativado.getNome())
								.descricao(reativado.getDescricao())
								.status(reativado.getStatus())
								.diretorId(reativado.getDiretor() != null ? reativado.getDiretor().getId() : null)
								.build();
						return ResponseEntity.ok(vm);
					}
					return ResponseEntity.status(409).body((SetorResponseViewModel) null);
				})
				.orElseGet(() -> {
					Setor s = new Setor();
					s.setNome(req.getNome());
					s.setDescricao(req.getDescricao());
					s.setStatus(true);
					Setor criado = setorService.criar(s, req.getDiretorId());
					SetorResponseViewModel vm = SetorResponseViewModel.builder()
							.id(criado.getId())
							.nome(criado.getNome())
							.descricao(criado.getDescricao())
							.status(criado.getStatus())
							.diretorId(criado.getDiretor() != null ? criado.getDiretor().getId() : null)
							.build();
					return ResponseEntity.status(201).body(vm);
				});
	}

	@PutMapping("/{id}")
	public ResponseEntity<SetorResponseViewModel> atualizar(@PathVariable Long id,
			@Valid @RequestBody SetorUpdateRequestViewModel req) {
		return setorService.atualizar(id, req.getNome(), req.getDescricao(), req.getDiretorId())
				.map(atualizado -> ResponseEntity.ok(SetorResponseViewModel.builder()
						.id(atualizado.getId())
						.nome(atualizado.getNome())
						.descricao(atualizado.getDescricao())
						.status(atualizado.getStatus())
						.diretorId(atualizado.getDiretor() != null ? atualizado.getDiretor().getId() : null)
						.build()))
				.orElseGet(() -> ResponseEntity.status(404).body((SetorResponseViewModel) null));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		boolean desativado = setorService.desativar(id);
		return desativado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}/equipes")
	public ResponseEntity<List<EquipeListItemViewModel>> listarEquipesDoSetor(@PathVariable Long id) {
		return setorService.buscarPorId(id)
				.map(s -> ResponseEntity.ok(
						setorService.listarEquipesAtivasDoSetor(id).stream()
								.map(e -> EquipeListItemViewModel.builder()
										.id(e.getId())
										.nome(e.getNome())
										.gestor(String.valueOf(e.getGestor()))
										.build())
								.toList()
				))
				.orElseGet(() -> ResponseEntity.status(404).body((List<EquipeListItemViewModel>) null));
	}

	@GetMapping("/{id}/colaboradores")
	public ResponseEntity<List<ColaboradorListItemViewModel>> listarColaboradoresDoSetor(@PathVariable Long id) {
		return setorService.buscarPorId(id)
				.map(s -> ResponseEntity.ok(
						setorService.listarColaboradoresAtivosDoSetor(id).stream()
								.map(c -> ColaboradorListItemViewModel.builder()
										.id(c.getId())
										.nomeCompleto((c.getNome() + " " + c.getSobrenome()).trim())
										.email(c.getEmail())
										.cargo(c.getCargo() != null ? c.getCargo().getNome() : null)
										.build())
								.toList()
				))
				.orElseGet(() -> ResponseEntity.status(404).body((List<ColaboradorListItemViewModel>) null));
	}
}




