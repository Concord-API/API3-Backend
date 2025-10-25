package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.application.dto.ColaboradorCompetenciaDTO;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaUpdateItemRequestViewModel;
import com.concord.proficio.presentation.viewmodel.PerfilUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.PerfilResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorListResponseViewModel;
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

    @GetMapping
    public ResponseEntity<List<ColaboradorListResponseViewModel>> listarColaboradores() {
        List<ColaboradorListResponseViewModel> colaboradores = colaboradorService.listarTodosColaboradores()
                .stream()
                .map(dto -> ColaboradorListResponseViewModel.builder()
                        .id(dto.getId())
                        .nome(dto.getNome())
                        .sobrenome(dto.getSobrenome())
                        .email(dto.getEmail())
                        .dataNascimento(dto.getDataNascimento())
                        .genero(dto.getGenero())
                        .avatar(dto.getAvatar())
                        .capa(dto.getCapa())
                        .criadoEm(dto.getCriadoEm())
                        .atualizadoEm(dto.getAtualizadoEm())
                        .build())
                .toList();
        
        return ResponseEntity.ok(colaboradores);
    }

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<PerfilResponseViewModel> atualizarPerfilColaborador(
            @PathVariable Long id,
            @RequestBody PerfilUpdateRequestViewModel request) {
        
        // Converter ViewModel para DTO
        PerfilUpdateDTO perfilUpdate = PerfilUpdateDTO.builder()
                .nome(request.getNome())
                .sobrenome(request.getSobrenome())
                .email(request.getEmail())
                .dataNascimento(request.getDataNascimento())
                .genero(request.getGenero())
                .avatar(request.getAvatar())
                .capa(request.getCapa())
                .build();
        
        return colaboradorService.atualizarPerfil(id, perfilUpdate)
                .map(dto -> ResponseEntity.ok(PerfilResponseViewModel.builder()
                        .id(dto.getId())
                        .nome(dto.getNome())
                        .sobrenome(dto.getSobrenome())
                        .email(dto.getEmail())
                        .dataNascimento(dto.getDataNascimento())
                        .genero(dto.getGenero())
                        .avatar(dto.getAvatar())
                        .capa(dto.getCapa())
                        .criadoEm(dto.getCriadoEm())
                        .atualizadoEm(dto.getAtualizadoEm())
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }
}