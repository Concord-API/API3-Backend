package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.EquipeService;
import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.presentation.viewmodel.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeService equipeService;

    public EquipeController(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    @GetMapping
    public ResponseEntity<List<EquipeResponseViewModel>> listar(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "status", required = false, defaultValue = "active") String status) {

        List<Equipe> equipes = equipeService.buscar(q, status);
        List<EquipeResponseViewModel> vms = new ArrayList<>();

        for (Equipe e : equipes) {
            long colaboradoresCount = equipeService.contarColaboradoresAtivosDaEquipe(e.getId());
            EquipeResponseViewModel vm = EquipeResponseViewModel.builder()
                    .id(e.getId())
                    .nome(e.getNome())
                    .status(e.getStatus())
                    .gestorId(e.getGestor() != null ? e.getGestor().getId() : null)
                    .setorId(e.getSetor() != null ? e.getSetor().getId() : null)
                    .colaboradoresCount(colaboradoresCount) // ✅ Now includes member count
                    .build();
            vms.add(vm);
        }

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
                long colaboradoresCount = equipeService.contarColaboradoresAtivosDaEquipe(existing.getId());
                EquipeResponseViewModel vm = EquipeResponseViewModel.builder()
                        .id(existing.getId())
                        .nome(existing.getNome())
                        .status(existing.getStatus())
                        .gestorId(existing.getGestor() != null ? existing.getGestor().getId() : null)
                        .setorId(existing.getSetor() != null ? existing.getSetor().getId() : null)
                        .colaboradoresCount(colaboradoresCount)
                        .build();
                return ResponseEntity.ok(vm);
            }
            return ResponseEntity.status(409).build();
        }

        Equipe nova = new Equipe();
        nova.setNome(req.getNome());
        Equipe criada = equipeService.criar(nova, req.getSetorId(), req.getGestorId());
        long colaboradoresCount = equipeService.contarColaboradoresAtivosDaEquipe(criada.getId());
        EquipeResponseViewModel vm = EquipeResponseViewModel.builder()
                .id(criada.getId())
                .nome(criada.getNome())
                .status(criada.getStatus())
                .gestorId(criada.getGestor() != null ? criada.getGestor().getId() : null)
                .setorId(criada.getSetor() != null ? criada.getSetor().getId() : null)
                .colaboradoresCount(colaboradoresCount)
                .build();
        return ResponseEntity.status(201).body(vm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipeResponseViewModel> atualizar(@PathVariable Long id,
                                                             @Valid @RequestBody EquipeUpdateRequestViewModel req) {
        Optional<Equipe> optional = equipeService.atualizar(id, req.getNome(), req.getGestorId());

        if (optional.isPresent()) {
            Equipe atualizada = optional.get();
            long colaboradoresCount = equipeService.contarColaboradoresAtivosDaEquipe(atualizada.getId());
            EquipeResponseViewModel vm = EquipeResponseViewModel.builder()
                    .id(atualizada.getId())
                    .nome(atualizada.getNome())
                    .status(atualizada.getStatus())
                    .gestorId(atualizada.getGestor() != null ? atualizada.getGestor().getId() : null)
                    .setorId(atualizada.getSetor() != null ? atualizada.getSetor().getId() : null)
                    .colaboradoresCount(colaboradoresCount)
                    .build();
            return ResponseEntity.ok(vm);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean sucesso = equipeService.desativar(id);
        return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}