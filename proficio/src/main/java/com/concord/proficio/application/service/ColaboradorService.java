// ...existing code...
package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.presentation.dto.ColaboradorCompetenciaUpdateItemDTO;
import com.concord.proficio.presentation.dto.CompetenciaDTO;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorCompetenciaRepository;
import com.concord.proficio.infra.repositories.CompetenciaRepository;
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

    // listar competências como DTOs (para endpoint GET /api/colaboradores/{id}/competencias)
    public Optional<List<CompetenciaDTO>> listarCompetenciasDTO(Long colaboradorId) {
        if (colaboradorRepository.findById(colaboradorId).isEmpty()) {
            return Optional.empty();
        }
        List<ColaboradorCompetencia> competencias = colaboradorCompetenciaRepository.findByColaboradorId(colaboradorId);
        List<CompetenciaDTO> dtos = competencias.stream()
                .map(cc -> CompetenciaDTO.builder()
                        .id(cc.getCompetencia().getId())
                        .nome(cc.getCompetencia().getNome())
                        .tipo(cc.getCompetencia().getTipo())
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .build())
                .collect(Collectors.toList());
        return Optional.of(dtos);
    }

    // atualizar/adicionar lista de competências do colaborador (PATCH)
    @Transactional
    public Optional<List<CompetenciaDTO>> atualizarCompetencias(Long colaboradorId, List<ColaboradorCompetenciaUpdateItemDTO> items) {
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

    // remover uma competência específica do colaborador (verifica vínculo)
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

    // ...existing code...
}