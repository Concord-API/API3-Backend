package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.dto.ColaboradorCompetenciaDTO;
import com.concord.proficio.application.dto.ColaboradorPerfilDTO;
import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.dto.PerfilResponseDTO;
import com.concord.proficio.application.dto.ColaboradorListDTO;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorCompetenciaRepository;
import com.concord.proficio.infra.repositories.CompetenciaRepository;
import org.springframework.beans.BeanUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorCompetenciaRepository colaboradorCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository,
                              ColaboradorCompetenciaRepository colaboradorCompetenciaRepository,
                              CompetenciaRepository competenciaRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.colaboradorCompetenciaRepository = colaboradorCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
    }

    public Optional<List<ColaboradorCompetenciaDTO>> listarCompetenciasDTO(Long colaboradorId) {
        if (colaboradorRepository.findById(colaboradorId).isEmpty()) {
            return Optional.empty();
        }
        List<ColaboradorCompetencia> competencias = colaboradorCompetenciaRepository.findByColaboradorIdAndStatusTrue(colaboradorId);
        List<ColaboradorCompetenciaDTO> dtos = competencias.stream()
                .map(cc -> ColaboradorCompetenciaDTO.builder()
                        .id(cc.getId())
                        .idCompetencia(cc.getCompetencia().getId())
                        .nome(cc.getCompetencia().getNome())
                        .tipo(cc.getCompetencia().getTipo())
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .build())
                .collect(Collectors.toList());
        return Optional.of(dtos);
    }

    @Transactional
    public Optional<List<ColaboradorCompetenciaDTO>> atualizarCompetencias(Long colaboradorId, List<ColaboradorCompetenciaUpdateItemDTO> items) {
        Optional<Colaborador> optionalCol = colaboradorRepository.findById(colaboradorId);
        if (optionalCol.isEmpty()) {
            return Optional.empty();
        }
        Colaborador colaborador = optionalCol.get();

        for (ColaboradorCompetenciaUpdateItemDTO item : items) {
            Long compId = item.getCompetenciaId();
            Integer proef = item.getProeficiencia();
            Integer ordem = item.getOrdem();

            Competencia competencia = competenciaRepository.findById(compId)
                    .orElseThrow(() -> new IllegalArgumentException("Competencia não encontrada: " + compId));

            Optional<ColaboradorCompetencia> existente = colaboradorCompetenciaRepository
                    .findByColaboradorIdAndCompetenciaId(colaboradorId, compId);

            if (existente.isPresent()) {
                ColaboradorCompetencia cc = existente.get();
                if (proef != null) cc.setProeficiencia(proef);
                if (ordem != null) cc.setOrdem(ordem);
                cc.setStatus(true);
                colaboradorCompetenciaRepository.save(cc);
            } else {
                ColaboradorCompetencia novo = new ColaboradorCompetencia();
                novo.setColaborador(colaborador);
                novo.setCompetencia(competencia);
                novo.setProeficiencia(proef != null ? proef : 0);
                novo.setOrdem(ordem);
                novo.setStatus(true);
                colaboradorCompetenciaRepository.save(novo);
            }
        }

        return listarCompetenciasDTO(colaboradorId);
    }

    @Transactional
    public boolean removerCompetencia(Long colaboradorId, Long colaboradorCompetenciaId) {
        Optional<ColaboradorCompetencia> optional = colaboradorCompetenciaRepository.findById(colaboradorCompetenciaId);
        if (optional.isEmpty()) return false;
        ColaboradorCompetencia cc = optional.get();
        if (cc.getColaborador() == null || !colaboradorId.equals(cc.getColaborador().getId())) {
            return false;
        }
        cc.setStatus(false);
        colaboradorCompetenciaRepository.save(cc);
        return true;
    }

    @Transactional
    public Optional<PerfilResponseDTO> atualizarPerfil(Long colaboradorId, PerfilUpdateDTO perfilUpdate) {
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(colaboradorId);
        if (optionalColaborador.isEmpty()) {
            return Optional.empty();
        }

        Colaborador colaborador = optionalColaborador.get();
        
        // Atualização parcial usando BeanUtils, ignorando valores nulos
        BeanUtils.copyProperties(perfilUpdate, colaborador, getNullPropertyNames(perfilUpdate));
        
        Colaborador colaboradorAtualizado = colaboradorRepository.save(colaborador);
        
        return Optional.of(PerfilResponseDTO.builder()
                .id(colaboradorAtualizado.getId())
                .nome(colaboradorAtualizado.getNome())
                .sobrenome(colaboradorAtualizado.getSobrenome())
                .email(colaboradorAtualizado.getEmail())
                .dataNascimento(colaboradorAtualizado.getDataNascimento())
                .genero(colaboradorAtualizado.getGenero())
                .avatar(colaboradorAtualizado.getAvatar())
                .capa(colaboradorAtualizado.getCapa())
                .criadoEm(colaboradorAtualizado.getCriadoEm())
                .atualizadoEm(colaboradorAtualizado.getAtualizadoEm())
                .build());
    }

    public List<ColaboradorListDTO> listarTodosColaboradores() {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        return colaboradores.stream()
                .map(colaborador -> ColaboradorListDTO.builder()
                        .id(colaborador.getId())
                        .nome(colaborador.getNome())
                        .sobrenome(colaborador.getSobrenome())
                        .email(colaborador.getEmail())
                        .dataNascimento(colaborador.getDataNascimento())
                        .genero(colaborador.getGenero())
                        .role(colaborador.getRole())
                        .nomeCargo(colaborador.getCargo() != null ? colaborador.getCargo().getNome() : null)
                        .idEquipe(colaborador.getEquipe() != null ? colaborador.getEquipe().getId() : null)
                        .idSetor(colaborador.getEquipe() != null && colaborador.getEquipe().getSetor() != null ? colaborador.getEquipe().getSetor().getId() : null)
                        .avatar(colaborador.getAvatar())
                        .capa(colaborador.getCapa())
                        .criadoEm(colaborador.getCriadoEm())
                        .atualizadoEm(colaborador.getAtualizadoEm())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<PerfilResponseDTO> obterPerfil(Long colaboradorId) {
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(colaboradorId);
        if (optionalColaborador.isEmpty()) {
            return Optional.empty();
        }

        Colaborador colaborador = optionalColaborador.get();
        return Optional.of(PerfilResponseDTO.builder()
                .id(colaborador.getId())
                .nome(colaborador.getNome())
                .sobrenome(colaborador.getSobrenome())
                .email(colaborador.getEmail())
                .dataNascimento(colaborador.getDataNascimento())
                .genero(colaborador.getGenero())
                .avatar(colaborador.getAvatar())
                .capa(colaborador.getCapa())
                .criadoEm(colaborador.getCriadoEm())
                .atualizadoEm(colaborador.getAtualizadoEm())
                .build());
    }

    public Optional<ColaboradorPerfilDTO> obterPerfilCompleto(Long colaboradorId) {
        Optional<Colaborador> optionalColaborador = colaboradorRepository.findById(colaboradorId);
        if (optionalColaborador.isEmpty()) {
            return Optional.empty();
        }
        Colaborador colab = optionalColaborador.get();

        ColaboradorPerfilDTO.Cargo cargoDTO = null;
        if (colab.getCargo() != null) {
            cargoDTO = ColaboradorPerfilDTO.Cargo.builder()
                    .id(colab.getCargo().getId())
                    .nome(colab.getCargo().getNome())
                    .descricao(colab.getCargo().getDescricao())
                    .status(colab.getCargo().getStatus())
                    .build();
        }

        ColaboradorPerfilDTO.Setor setorDTO = null;
        if (colab.getEquipe() != null && colab.getEquipe().getSetor() != null) {
            setorDTO = ColaboradorPerfilDTO.Setor.builder()
                    .id(colab.getEquipe().getSetor().getId())
                    .nome(colab.getEquipe().getSetor().getNome())
                    .descricao(colab.getEquipe().getSetor().getDescricao())
                    .status(colab.getEquipe().getSetor().getStatus())
                    .build();
        }
        ColaboradorPerfilDTO.Equipe equipeDTO = null;
        if (colab.getEquipe() != null) {
            equipeDTO = ColaboradorPerfilDTO.Equipe.builder()
                    .id(colab.getEquipe().getId())
                    .nome(colab.getEquipe().getNome())
                    .status(colab.getEquipe().getStatus())
                    .setor(setorDTO)
                    .build();
        }

        List<ColaboradorCompetencia> list = colaboradorCompetenciaRepository.findByColaboradorIdAndStatusTrue(colaboradorId);
        List<ColaboradorPerfilDTO.ColaboradorCompetenciaFull> competencias = list.stream().map(cc ->
                ColaboradorPerfilDTO.ColaboradorCompetenciaFull.builder()
                        .id(cc.getId())
                        .colaboradorId(colaboradorId)
                        .competenciaId(cc.getCompetencia() != null ? cc.getCompetencia().getId() : null)
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .competencia(cc.getCompetencia() == null ? null : ColaboradorPerfilDTO.CompetenciaItem.builder()
                                .id(cc.getCompetencia().getId())
                                .nome(cc.getCompetencia().getNome())
                                .tipo(cc.getCompetencia().getTipo())
                                .build())
                        .build()
        ).collect(Collectors.toList());

        ColaboradorPerfilDTO dto = ColaboradorPerfilDTO.builder()
                .id(colab.getId())
                .nome(colab.getNome())
                .sobrenome(colab.getSobrenome())
                .email(colab.getEmail())
                .status(colab.getStatus())
                .role(colab.getRole() != null ? colab.getRole().name() : null)
                    .dataNascimento(colab.getDataNascimento())
                .criadoEm(colab.getCriadoEm())
                .atualizadoEm(colab.getAtualizadoEm())
                .avatar(colab.getAvatar())
                .capa(colab.getCapa())
                .cargo(cargoDTO)
                .equipe(equipeDTO)
                .competencias(competencias)
                .build();
        return Optional.of(dto);
    }

    @Transactional
    public boolean atualizarOrdemCompetenciasPerfil(Long colaboradorId, java.util.List<com.concord.proficio.presentation.viewmodel.PerfilCompetenciaOrderItemViewModel> items) {
        Optional<Colaborador> optionalCol = colaboradorRepository.findById(colaboradorId);
        if (optionalCol.isEmpty()) return false;

        for (var it : items) {
            Long itemId = it.getId();
            Long competenciaId = it.getId_competencia();
            Integer ordem = it.getOrdem();

            ColaboradorCompetencia alvo = null;
            if (itemId != null) {
                var opt = colaboradorCompetenciaRepository.findById(itemId);
                if (opt.isPresent() && opt.get().getColaborador() != null && colaboradorId.equals(opt.get().getColaborador().getId())) {
                    alvo = opt.get();
                }
            }
            if (alvo == null && competenciaId != null) {
                alvo = colaboradorCompetenciaRepository.findByColaboradorIdAndCompetenciaId(colaboradorId, competenciaId).orElse(null);
            }
            if (alvo == null && competenciaId != null) {
                // cria se não existir
                Competencia comp = competenciaRepository.findById(competenciaId)
                        .orElseThrow(() -> new IllegalArgumentException("Competencia não encontrada: " + competenciaId));
                alvo = new ColaboradorCompetencia();
                alvo.setColaborador(optionalCol.get());
                alvo.setCompetencia(comp);
                alvo.setProeficiencia(0);
            }
            if (alvo != null) {
                if (ordem != null) alvo.setOrdem(ordem);
                colaboradorCompetenciaRepository.save(alvo);
            }
        }
        try {
            Colaborador col = optionalCol.get();
            col.setAtualizadoEm(java.time.LocalDateTime.now());
            colaboradorRepository.save(col);
        } catch (Exception ignored) {}
        return true;
    }

    /**
     * Retorna um array com os nomes das propriedades que são nulas no objeto fornecido
     */
    private String[] getNullPropertyNames(Object source) {
        List<String> nullPropertyNames = new ArrayList<>();
        Field[] fields = source.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value == null) {
                    nullPropertyNames.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                // Ignora campos inacessíveis
            }
        }
        
        return nullPropertyNames.toArray(new String[0]);
    }
}