package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.presentation.viewmodel.PerfilUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.PerfilResponseViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PerfilController {

    private final ColaboradorService colaboradorService;

    public PerfilController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PatchMapping("/perfil")
    public ResponseEntity<PerfilResponseViewModel> atualizarPerfil(
            @RequestBody PerfilUpdateRequestViewModel request,
            Authentication authentication) {
        
        // Obter o ID do usuário logado
        Long colaboradorId = ((com.concord.proficio.domain.entities.Colaborador) authentication.getPrincipal()).getId();
        
        byte[] avatarBytes = decodeDataUrlToBytes(request.getAvatar());
        byte[] capaBytes = decodeDataUrlToBytes(request.getCapa());
        PerfilUpdateDTO perfilUpdate = PerfilUpdateDTO.builder()
                .nome(request.getNome())
                .sobrenome(request.getSobrenome())
                .email(request.getEmail())
                .dataNascimento(request.getDataNascimento())
                .genero(request.getGenero())
                .avatar(avatarBytes)
                .capa(capaBytes)
                .build();
        
        return colaboradorService.atualizarPerfil(colaboradorId, perfilUpdate)
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
    private byte[] decodeDataUrlToBytes(String maybeDataUrl) {
        if (maybeDataUrl == null || maybeDataUrl.isBlank()) return null;
        try {
            String raw = maybeDataUrl.trim();
            int commaIdx = raw.indexOf(',');
            String base64 = (raw.startsWith("data:") && commaIdx > 0) ? raw.substring(commaIdx + 1) : raw;
            return java.util.Base64.getDecoder().decode(base64);
        } catch (Exception e) {
            return null;
        }
    }
}
