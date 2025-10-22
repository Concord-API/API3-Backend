package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.application.dto.ColaboradorCompetenciaDTO;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaUpdateItemRequestViewModel;
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


    @GetMapping("/{id}/competencias")
    public ResponseEntity<List<ColaboradorCompetenciaResponseViewModel>> listarCompetencias(@PathVariable Long id) {
        return colaboradorService.listarCompetenciasDTO(id)
                .map(dtos -> ResponseEntity.ok(
                        dtos.stream().map(dto -> ColaboradorCompetenciaResponseViewModel.builder()
                                .id(dto.getId())
                                .nome(dto.getNome())
                                .tipo(dto.getTipo())
                                .proeficiencia(dto.getProeficiencia())
                                .ordem(dto.getOrdem())
                                .build())
                                .toList()
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/competencias")
    public ResponseEntity<List<ColaboradorCompetenciaResponseViewModel>> atualizarCompetencias(
            @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody ColaboradorCompetenciaUpdateRequestViewModel request) {

        if (request == null || request.getItems() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ColaboradorCompetenciaUpdateItemDTO> items = request.getItems().stream()
                .map(vm -> new ColaboradorCompetenciaUpdateItemDTO(
                        vm.getCompetenciaId(), vm.getProeficiencia(), vm.getOrdem()
                ))
                .toList();

        return colaboradorService.atualizarCompetencias(id, items)
                .map(dtos -> ResponseEntity.ok(
                        dtos.stream().map(dto -> ColaboradorCompetenciaResponseViewModel.builder()
                                .id(dto.getId())
                                .nome(dto.getNome())
                                .tipo(dto.getTipo())
                                .proeficiencia(dto.getProeficiencia())
                                .ordem(dto.getOrdem())
                                .build())
                                .toList()
                ))
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