package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.SetorService;
import com.concord.proficio.domain.entities.Setor;
import com.concord.proficio.presentation.viewmodel.SetorCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.SetorResponseViewModel;
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
	public ResponseEntity<List<SetorResponseViewModel>> listar() {
		List<SetorResponseViewModel> vms = setorService.listarTodos().stream()
				.map(s -> SetorResponseViewModel.builder()
						.id(s.getId())
						.nome(s.getNome())
						.descricao(s.getDescricao())
						.status(s.getStatus())
						.build())
				.collect(Collectors.toList());
		return ResponseEntity.ok(vms);
	}

	@PostMapping
	public ResponseEntity<SetorResponseViewModel> criar(@Valid @RequestBody SetorCreateRequestViewModel req) {
		if (setorService.buscarPorNome(req.getNome()).isPresent()) {
			return ResponseEntity.status(409).build();
		}
		Setor s = new Setor();
		s.setNome(req.getNome());
		s.setDescricao(req.getDescricao());
		s.setStatus(true);
		Setor criado = setorService.criar(s);
		SetorResponseViewModel vm = SetorResponseViewModel.builder()
				.id(criado.getId())
				.nome(criado.getNome())
				.descricao(criado.getDescricao())
				.status(criado.getStatus())
				.build();
		return ResponseEntity.status(201).body(vm);
	}
}




