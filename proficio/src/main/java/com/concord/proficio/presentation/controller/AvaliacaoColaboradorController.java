package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.AvaliacaoColaboradorDTO;
import com.concord.proficio.application.service.AvaliacaoColaboradorService;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorResponseViewModel;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorUpdateRequestViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoColaboradorController {

    private final AvaliacaoColaboradorService avaliacaoColaboradorService;

    public AvaliacaoColaboradorController(AvaliacaoColaboradorService avaliacaoColaboradorService) {
        this.avaliacaoColaboradorService = avaliacaoColaboradorService;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoColaboradorResponseViewModel> criar(
            @Valid @RequestBody AvaliacaoColaboradorCreateRequestViewModel request) {
        
        return avaliacaoColaboradorService.criar(
                request.getAvaliadorId(),
                request.getAvaliadoId(),
                request.getResumo(),
                request.getCompetenciaId(),
                request.getStatus(),
                request.getPublico()
        )
        .map(dto -> ResponseEntity.status(201).body(mapToViewModel(dto)))
        .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoColaboradorResponseViewModel> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoColaboradorUpdateRequestViewModel request) {
        
        return avaliacaoColaboradorService.atualizar(
                id,
                request.getResumo(),
                request.getCompetenciaId(),
                request.getStatus(),
                request.getPublico()
        )
        .map(dto -> ResponseEntity.ok(mapToViewModel(dto)))
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoColaboradorResponseViewModel>> listar() {
        List<AvaliacaoColaboradorDTO> dtos = avaliacaoColaboradorService.listarTodas();
        
        List<AvaliacaoColaboradorResponseViewModel> vms = dtos.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/colaborador/{id}")
    public ResponseEntity<List<AvaliacaoColaboradorResponseViewModel>> listarPorColaborador(
            @PathVariable Long id) {
        
        List<AvaliacaoColaboradorDTO> dtos = avaliacaoColaboradorService.listarPorColaborador(id);
        
        List<AvaliacaoColaboradorResponseViewModel> vms = dtos.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vms);
    }

    private AvaliacaoColaboradorResponseViewModel mapToViewModel(AvaliacaoColaboradorDTO dto) {
        return AvaliacaoColaboradorResponseViewModel.builder()
                .id(dto.getId())
                .avaliadorId(dto.getAvaliadorId())
                .avaliadorNome(dto.getAvaliadorNome())
                .avaliadoId(dto.getAvaliadoId())
                .avaliadoNome(dto.getAvaliadoNome())
                .resumo(dto.getResumo())
                .competenciaId(dto.getCompetenciaId())
                .competenciaNome(dto.getCompetenciaNome())
                .status(dto.getStatus())
                .publico(dto.getPublico())
                .build();
    }
}

