package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.AvaliacaoColaboradorDTO;
import com.concord.proficio.application.service.AvaliacaoColaboradorService;
import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorCreateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorResponseViewModel;
import com.concord.proficio.presentation.viewmodel.AvaliacaoColaboradorUpdateRequestViewModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                request.getPublico(),
                request.getNota()
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
                request.getPublico(),
                request.getNota()
        )
        .map(dto -> ResponseEntity.ok(mapToViewModel(dto)))
        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<AvaliacaoColaboradorResponseViewModel>> listarMinhasAvaliacoes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Colaborador colaboradorLogado = (Colaborador) authentication.getPrincipal();
        
        List<AvaliacaoColaboradorDTO> dtos = avaliacaoColaboradorService.listarPorColaborador(colaboradorLogado.getId());
        
        List<AvaliacaoColaboradorResponseViewModel> vms = dtos.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/feitas")
    public ResponseEntity<List<AvaliacaoColaboradorResponseViewModel>> listarAvaliacoesFeitas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Colaborador colaboradorLogado = (Colaborador) authentication.getPrincipal();
        
        List<AvaliacaoColaboradorDTO> dtos = avaliacaoColaboradorService.listarPorAvaliador(colaboradorLogado.getId());
        
        List<AvaliacaoColaboradorResponseViewModel> vms = dtos.stream()
                .map(this::mapToViewModel)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoColaboradorResponseViewModel> buscarPorId(@PathVariable Long id) {
        return avaliacaoColaboradorService.buscarPorId(id)
                .map(dto -> ResponseEntity.ok(mapToViewModel(dto)))
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping
    public ResponseEntity<List<AvaliacaoColaboradorResponseViewModel>> listar() {
        List<AvaliacaoColaboradorDTO> dtos = avaliacaoColaboradorService.listarTodas();
        
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
                .nota(dto.getNota())
                .competenciaId(dto.getCompetenciaId())
                .competenciaNome(dto.getCompetenciaNome())
                .status(dto.getStatus())
                .publico(dto.getPublico())
                .criadoEm(dto.getCriadoEm())
                .atualizadoEm(dto.getAtualizadoEm())
                .build();
    }
}

