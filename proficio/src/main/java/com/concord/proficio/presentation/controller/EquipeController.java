package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.EquipeService;
import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.presentation.viewmodel.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public ResponseEntity<List<EquipeResponseViewModel>> listar(
            @RequestParam(value = "q", required = false) String q) {
        List<Equipe> equipes = equipeService.buscar(q);
        List<EquipeResponseViewModel> vms = equipes.stream()
                .map(this::toViewModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vms);
    }

    @PostMapping
    public ResponseEntity<EquipeResponseViewModel> criar(@Valid @RequestBody EquipeCreateRequestViewModel req) {
        Optional<Equipe> optional = equipeService.buscarPorNome(req.getNome());

        if (optional.isPresent()) {
            Equipe existing = optional.get();
            if (Boolean.FALSE.equals(existing.getStatus())) {
                existing.setNome(req.getNome());
                equipeService.criar(existing, req.getSetorId(), req.getGestorId());
                return ResponseEntity.ok(toViewModel(existing));
            }
            return ResponseEntity.status(409).build();
        }

        Equipe nova = new Equipe();
        nova.setNome(req.getNome());
        Equipe criada = equipeService.criar(nova, req.getSetorId(), req.getGestorId());
        return ResponseEntity.status(200).body(toViewModel(criada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponseViewModel> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody EquipeUpdateRequestViewModel req) {
        Optional<Equipe> optional = equipeService.atualizar(id, req.getNome(), req.getGestorId());

        if (optional.isPresent()) {
            return ResponseEntity.ok(toViewModel(optional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean sucesso = equipeService.desativar(id);
        return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private EquipeResponseViewModel toViewModel(Equipe e) {
        return EquipeResponseViewModel.builder()
                .id(e.getId())
                .nome(e.getNome())
                .status(e.getStatus())
                .gestorId(e.getGestor() != null ? e.getGestor().getId() : null)
                .setorId(e.getSetor() != null ? e.getSetor().getId() : null)
                .build();
    }
}