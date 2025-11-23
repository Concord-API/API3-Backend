package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.CompetenciaService;
import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.presentation.viewmodel.CompetenciaCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.CompetenciaResponseViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/competencias")
public class CompetenciaController {

    private final CompetenciaService competenciaService;

    public CompetenciaController(CompetenciaService competenciaService) {
        this.competenciaService = competenciaService;
    }

    @GetMapping
    public ResponseEntity<List<CompetenciaResponseViewModel>> listar() {
        List<CompetenciaResponseViewModel> vms = competenciaService.listarTodos().stream()
                .map(c -> CompetenciaResponseViewModel.builder()
                        .id(c.getId())
                        .nome(c.getNome())
                        .tipo(c.getTipo())
                        .aprovada(c.getAprovada())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(vms);
    }

    @PostMapping
    public ResponseEntity<CompetenciaResponseViewModel> criar(@Valid @RequestBody CompetenciaCreateRequestViewModel req) {
        Competencia c = new Competencia();
        c.setNome(req.getNome());
        c.setTipo(req.getTipo());
        c.setStatus(true);
        Competencia criado = competenciaService.criar(c);
        CompetenciaResponseViewModel vm = CompetenciaResponseViewModel.builder()
                .id(criado.getId())
                .nome(criado.getNome())
                .tipo(criado.getTipo())
                .aprovada(criado.getAprovada())
                .build();
        return ResponseEntity.status(201).body(vm);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Long id) {
        if (competenciaService.aprovar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}