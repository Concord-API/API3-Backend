package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.application.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.application.dto.ColaboradorCompetenciaDTO;
import com.concord.proficio.application.dto.PerfilUpdateDTO;
import com.concord.proficio.application.dto.PerfilResponseDTO;
import com.concord.proficio.application.dto.ColaboradorListDTO;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorCompetenciaRepository;
import com.concord.proficio.infra.repositories.CompetenciaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
        List<ColaboradorCompetencia> competencias = colaboradorCompetenciaRepository.findByColaboradorId(colaboradorId);
        List<ColaboradorCompetenciaDTO> dtos = competencias.stream()
                .map(cc -> ColaboradorCompetenciaDTO.builder()
                        .id(cc.getCompetencia().getId())
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
                cc.setProeficiencia(proef);
                cc.setOrdem(ordem);
                colaboradorCompetenciaRepository.save(cc);
            } else {
                ColaboradorCompetencia novo = new ColaboradorCompetencia();
                novo.setColaborador(colaborador);
                novo.setCompetencia(competencia);
                novo.setProeficiencia(proef != null ? proef : 0);
                novo.setOrdem(ordem);
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
        colaboradorCompetenciaRepository.deleteById(colaboradorCompetenciaId);
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