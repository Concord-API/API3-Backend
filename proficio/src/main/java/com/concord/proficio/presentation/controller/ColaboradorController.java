package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCompetenciaUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.PerfilUpdateRequestViewModel;
import com.concord.proficio.presentation.viewmodel.PerfilResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorListResponseViewModel;
import com.concord.proficio.presentation.viewmodel.ColaboradorCreateRequestViewModel;
import com.concord.proficio.domain.entities.Cargo;
import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import com.concord.proficio.domain.enums.GeneroEnum;
import com.concord.proficio.infra.repositories.CargoRepository;
import com.concord.proficio.infra.repositories.EquipeRepository;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import java.util.*;
import com.concord.proficio.application.dto.ColaboradorPerfilDTO;
import com.concord.proficio.presentation.viewmodel.ColaboradorPerfilResponseViewModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final CargoRepository cargoRepository;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final PasswordEncoder passwordEncoder;

    public ColaboradorController(ColaboradorService colaboradorService,
                                 CargoRepository cargoRepository,
                                 EquipeRepository equipeRepository,
                                 ColaboradorRepository colaboradorRepository,
                                 PasswordEncoder passwordEncoder) {
        this.colaboradorService = colaboradorService;
        this.cargoRepository = cargoRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.passwordEncoder = passwordEncoder;
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
                        .role(dto.getRole() != null ? dto.getRole().name() : null)
                        .cargoNome(dto.getNomeCargo())
                        .idEquipe(dto.getIdEquipe())
                        .idSetor(dto.getIdSetor())
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
        
        colaboradorService.atualizarPerfil(id, perfilUpdate);
        if (request.getCompetencias() != null && !request.getCompetencias().isEmpty()) {
            colaboradorService.atualizarOrdemCompetenciasPerfil(id, request.getCompetencias());
        }
        return colaboradorService.obterPerfil(id)
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

    @PostMapping
    public ResponseEntity<ColaboradorListResponseViewModel> criar(@jakarta.validation.Valid @RequestBody ColaboradorCreateRequestViewModel req) {
        Cargo cargo = cargoRepository.findById(req.getIdCargo()).orElse(null);
        Equipe equipe = equipeRepository.findById(req.getIdEquipe()).orElse(null);
        if (cargo == null || equipe == null) {
            return ResponseEntity.badRequest().build();
        }
        if (colaboradorRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(409).build();
        }
        Colaborador novo = new Colaborador();
        novo.setNome(req.getNome());
        novo.setSobrenome(req.getSobrenome());
        novo.setEmail(req.getEmail());
        novo.setSenha(passwordEncoder.encode(req.getSenha()));
        if (req.getGenero() != null) {
            try { novo.setGenero(GeneroEnum.valueOf(req.getGenero())); } catch (Exception ignored) {}
        }
        try { novo.setRole(ColaboradorRoleEnum.valueOf(req.getRole())); } catch (Exception ignored) { novo.setRole(ColaboradorRoleEnum.Colaborador); }
        novo.setStatus(req.getStatus() != null ? req.getStatus() : true);
        novo.setCargo(cargo);
        novo.setEquipe(equipe);
        Colaborador salvo = colaboradorRepository.save(novo);

        ColaboradorListResponseViewModel vm = ColaboradorListResponseViewModel.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .sobrenome(salvo.getSobrenome())
                .email(salvo.getEmail())
                .genero(salvo.getGenero())
                .role(salvo.getRole() != null ? salvo.getRole().name() : null)
                .cargoNome(salvo.getCargo() != null ? salvo.getCargo().getNome() : null)
                .idEquipe(salvo.getEquipe() != null ? salvo.getEquipe().getId() : null)
                .idSetor(salvo.getEquipe() != null && salvo.getEquipe().getSetor() != null ? salvo.getEquipe().getSetor().getId() : null)
                .avatar(salvo.getAvatar())
                .capa(salvo.getCapa())
                .criadoEm(salvo.getCriadoEm())
                .atualizadoEm(salvo.getAtualizadoEm())
                .build();
        return ResponseEntity.status(201).body(vm);
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

    @GetMapping("/{id}/perfil")
    public ResponseEntity<ColaboradorPerfilResponseViewModel> obterPerfilColaborador(@PathVariable Long id) {
        return colaboradorService.obterPerfilCompleto(id)
                .map(dto -> ResponseEntity.ok(mapToVM(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ColaboradorPerfilResponseViewModel mapToVM(ColaboradorPerfilDTO dto) {
        ColaboradorPerfilResponseViewModel.CargoVM cargoVM = null;
        if (dto.getCargo() != null) {
            cargoVM = ColaboradorPerfilResponseViewModel.CargoVM.builder()
                    .id_cargo(dto.getCargo().getId())
                    .nome_cargo(dto.getCargo().getNome())
                    .desc_cargo(dto.getCargo().getDescricao())
                    .status(dto.getCargo().getStatus())
                    .build();
        }
        ColaboradorPerfilResponseViewModel.SetorVM setorVM = null;
        if (dto.getEquipe() != null && dto.getEquipe().getSetor() != null) {
            setorVM = ColaboradorPerfilResponseViewModel.SetorVM.builder()
                    .id_setor(dto.getEquipe().getSetor().getId())
                    .nome_setor(dto.getEquipe().getSetor().getNome())
                    .desc_setor(dto.getEquipe().getSetor().getDescricao())
                    .status(dto.getEquipe().getSetor().getStatus())
                    .build();
        }
        ColaboradorPerfilResponseViewModel.EquipeVM equipeVM = null;
        if (dto.getEquipe() != null) {
            equipeVM = ColaboradorPerfilResponseViewModel.EquipeVM.builder()
                    .id_equipe(dto.getEquipe().getId())
                    .nome_equipe(dto.getEquipe().getNome())
                    .status(dto.getEquipe().getStatus())
                    .id_setor(dto.getEquipe().getSetor() != null ? dto.getEquipe().getSetor().getId() : null)
                    .setor(setorVM)
                    .build();
        }

        List<ColaboradorPerfilResponseViewModel.ColaboradorCompetenciaFullVM> comps = new ArrayList<>();
        if (dto.getCompetencias() != null) {
            for (var cc : dto.getCompetencias()) {
                ColaboradorPerfilResponseViewModel.CompetenciaItemVM compVM = null;
                if (cc.getCompetencia() != null) {
                    compVM = ColaboradorPerfilResponseViewModel.CompetenciaItemVM.builder()
                            .id_competencia(cc.getCompetencia().getId())
                            .nome(cc.getCompetencia().getNome())
                            .tipo(cc.getCompetencia().getTipo())
                            .build();
                }
                comps.add(ColaboradorPerfilResponseViewModel.ColaboradorCompetenciaFullVM.builder()
                        .id(cc.getId())
                        .id_colaborador(dto.getId())
                        .id_competencia(cc.getCompetenciaId())
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .competencia(compVM)
                        .build());
            }
        }

        return ColaboradorPerfilResponseViewModel.builder()
                .id_colaborador(dto.getId())
                .nome(dto.getNome())
                .sobrenome(dto.getSobrenome())
                .email(dto.getEmail())
                .status(dto.getStatus())
                .role(dto.getRole())
                .criado_em(dto.getCriadoEm() != null ? dto.getCriadoEm().toString() : null)
                .atualizado_em(dto.getAtualizadoEm() != null ? dto.getAtualizadoEm().toString() : null)
                .avatar(dto.getAvatar() != null ? Base64.getEncoder().encodeToString(dto.getAvatar()) : null)
                .capa(dto.getCapa() != null ? Base64.getEncoder().encodeToString(dto.getCapa()) : null)
                .id_cargo(dto.getCargo() != null ? dto.getCargo().getId() : null)
                .cargo(cargoVM)
                .id_equipe(dto.getEquipe() != null ? dto.getEquipe().getId() : null)
                .equipe(equipeVM)
                .competencias(comps)
                .build();
    }
}