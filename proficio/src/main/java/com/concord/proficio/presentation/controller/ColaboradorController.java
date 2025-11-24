package com.concord.proficio.presentation.controller;

import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.service.ColaboradorService;
import com.concord.proficio.presentation.viewmodel.*;
import com.concord.proficio.domain.entities.Cargo;
import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.enums.GeneroEnum;
import com.concord.proficio.infra.repositories.CargoRepository;
import com.concord.proficio.infra.repositories.EquipeRepository;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.*;
import com.concord.proficio.application.dto.ColaboradorPerfilDTO;
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
                                .idCompetencia(dto.getIdCompetencia())
                                .nome(dto.getNome())
                                .tipo(dto.getTipo())
                                .proeficiencia(dto.getProeficiencia())
                                .ordem(dto.getOrdem())
                                .certificado(dto.getCertificado())
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
                        vm.getCompetenciaId(), vm.getProeficiencia(), vm.getOrdem(), vm.getCertificado()
                ))
                .toList();

        return colaboradorService.atualizarCompetencias(id, items)
                .map(dtos -> ResponseEntity.ok(
                        dtos.stream().map(dto -> ColaboradorCompetenciaResponseViewModel.builder()
                                .id(dto.getId())
                                .idCompetencia(dto.getIdCompetencia())
                                .nome(dto.getNome())
                                .tipo(dto.getTipo())
                                .proeficiencia(dto.getProeficiencia())
                                .ordem(dto.getOrdem())
                                .certificado(dto.getCertificado())
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

    @GetMapping("/{id}/competencias/{idItem}/certificado")
    public ResponseEntity<byte[]> obterCertificadoCompetencia(@PathVariable Long id, @PathVariable Long idItem) {
        return colaboradorService.obterCertificadoCompetencia(id, idItem)
                .map(bytes -> {
                    HttpHeaders headers = new HttpHeaders();
                    
                    MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
                    String filename = "certificado";
                    
                    if (bytes.length >= 4) {
                        if (bytes[0] == 0x25 && bytes[1] == 0x50 && bytes[2] == 0x44 && bytes[3] == 0x46) {
                            contentType = MediaType.APPLICATION_PDF;
                            filename = "certificado.pdf";
                        }
                        else if (bytes[0] == (byte)0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) {
                            contentType = MediaType.IMAGE_PNG;
                            filename = "certificado.png";
                        }
                        else if (bytes[0] == (byte)0xFF && bytes[1] == (byte)0xD8 && bytes[2] == (byte)0xFF) {
                            contentType = MediaType.IMAGE_JPEG;
                            filename = "certificado.jpg";
                        }
                        else if (bytes[0] == 0x47 && bytes[1] == 0x49 && bytes[2] == 0x46) {
                            contentType = MediaType.IMAGE_GIF;
                            filename = "certificado.gif";
                        }
                    }
                    
                    headers.setContentType(contentType);
                    headers.setContentLength(bytes.length);
                    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
                    return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
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
                        .role(dto.getRole() != null ? dto.getRole().getValue() : null)
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
        if (req.getDataNascimento() != null) {
            novo.setDataNascimento(req.getDataNascimento());
        }
        if (req.getGenero() != null) {
            try { novo.setGenero(GeneroEnum.valueOf(req.getGenero())); } catch (Exception ignored) {}
        }
        novo.setStatus(req.getStatus() != null ? req.getStatus() : true);
        novo.setCargo(cargo);
        novo.setEquipe(equipe);
        Colaborador salvo = colaboradorRepository.save(novo);

        ColaboradorListResponseViewModel vm = ColaboradorListResponseViewModel.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .sobrenome(salvo.getSobrenome())
                .email(salvo.getEmail())
                .dataNascimento(salvo.getDataNascimento())
                .genero(salvo.getGenero())
                .role(salvo.getRole() != null ? salvo.getRole().getValue() : null)
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
        CargoResponseViewModel cargoVM = null;
        if (dto.getCargo() != null) {
            cargoVM = CargoResponseViewModel.builder()
                    .id_cargo(dto.getCargo().getId())
                    .nome_cargo(dto.getCargo().getNome())
                    .desc_cargo(dto.getCargo().getDescricao())
                    .status(dto.getCargo().getStatus())
                    .build();
        }
        SetorSimpleViewModel setorVM = null;
        if (dto.getEquipe() != null && dto.getEquipe().getSetor() != null) {
            setorVM = SetorSimpleViewModel.builder()
                    .id_setor(dto.getEquipe().getSetor().getId())
                    .nome_setor(dto.getEquipe().getSetor().getNome())
                    .desc_setor(dto.getEquipe().getSetor().getDescricao())
                    .status(dto.getEquipe().getSetor().getStatus())
                    .build();
        }
        EquipeSimpleViewModel equipeVM = null;
        if (dto.getEquipe() != null) {
            equipeVM = EquipeSimpleViewModel.builder()
                    .id_equipe(dto.getEquipe().getId())
                    .nome_equipe(dto.getEquipe().getNome())
                    .status(dto.getEquipe().getStatus())
                    .id_setor(dto.getEquipe().getSetor() != null ? dto.getEquipe().getSetor().getId() : null)
                    .setor(setorVM)
                    .build();
        }

        List<ColaboradorCompetenciaFullViewModel> comps = new ArrayList<>();
        if (dto.getCompetencias() != null) {
            for (var cc : dto.getCompetencias()) {
                CompetenciaItemViewModel compVM = null;
                if (cc.getCompetencia() != null) {
                    compVM = CompetenciaItemViewModel.builder()
                            .id_competencia(cc.getCompetencia().getId())
                            .nome(cc.getCompetencia().getNome())
                            .tipo(cc.getCompetencia().getTipo())
                            .build();
                }
                comps.add(ColaboradorCompetenciaFullViewModel.builder()
                        .id(cc.getId())
                        .id_colaborador(dto.getId())
                        .id_competencia(cc.getCompetenciaId())
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .certificado(cc.getCertificado())
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
                .data_nasci(dto.getDataNascimento() != null ? dto.getDataNascimento().toString() : null)
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