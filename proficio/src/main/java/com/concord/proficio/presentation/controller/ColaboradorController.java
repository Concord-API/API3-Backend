package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.presentation.dto.ColaboradorCompetenciaUpdateDTO;
import com.concord.proficio.presentation.dto.CompetenciaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    // Endpoints relacionados a competências do colaborador

    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaDTO>> listarCompetencias(@PathVariable Long id) {
        return colaboradorService.listarCompetenciasDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/competencias")
    public ResponseEntity<List<CompetenciaDTO>> atualizarCompetencias(
            @PathVariable Long id,
            @RequestBody ColaboradorCompetenciaUpdateDTO request) {

        if (request == null || request.getItems() == null) {
            return ResponseEntity.badRequest().build();
        }

        return colaboradorService.atualizarCompetencias(id, request.getItems())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/competencias/{idItem}")
    public ResponseEntity<Void> removerCompetencia(@PathVariable Long id, @PathVariable Long idItem) {
        boolean removed = colaboradorService.removerCompetencia(id, idItem);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}