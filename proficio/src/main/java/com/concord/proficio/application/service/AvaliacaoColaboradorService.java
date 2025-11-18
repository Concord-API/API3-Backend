package com.concord.proficio.application.service;

import com.concord.proficio.application.dto.AvaliacaoColaboradorDTO;
import com.concord.proficio.domain.entities.AvaliacaoColaborador;
import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.infra.repositories.AvaliacaoColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.CompetenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvaliacaoColaboradorService {

    private final AvaliacaoColaboradorRepository avaliacaoColaboradorRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final CompetenciaRepository competenciaRepository;

    public AvaliacaoColaboradorService(AvaliacaoColaboradorRepository avaliacaoColaboradorRepository,
                                      ColaboradorRepository colaboradorRepository,
                                      CompetenciaRepository competenciaRepository) {
        this.avaliacaoColaboradorRepository = avaliacaoColaboradorRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.competenciaRepository = competenciaRepository;
    }

    @Transactional
    public Optional<AvaliacaoColaboradorDTO> criar(Long avaliadorId, Long avaliadoId, String resumo, 
                                                   Long competenciaId, Boolean status, Boolean publico) {
        Colaborador avaliador = colaboradorRepository.findById(avaliadorId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliador não encontrado com ID: " + avaliadorId));
        
        Colaborador avaliado = colaboradorRepository.findById(avaliadoId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliado não encontrado com ID: " + avaliadoId));
        
        Competencia competencia = competenciaRepository.findById(competenciaId)
                .orElseThrow(() -> new IllegalArgumentException("Competência não encontrada com ID: " + competenciaId));

        AvaliacaoColaborador avaliacao = new AvaliacaoColaborador();
        avaliacao.setAvaliador(avaliador);
        avaliacao.setAvaliado(avaliado);
        avaliacao.setResumo(resumo);
        avaliacao.setCompetencia(competencia);
        avaliacao.setStatus(status != null ? status : true);
        avaliacao.setPublico(publico != null ? publico : false);

        AvaliacaoColaborador salva = avaliacaoColaboradorRepository.save(avaliacao);
        return Optional.of(mapToDTO(salva));
    }

    @Transactional
    public Optional<AvaliacaoColaboradorDTO> atualizar(Long id, String resumo, Long competenciaId, 
                                                        Boolean status, Boolean publico) {
        Optional<AvaliacaoColaborador> optional = avaliacaoColaboradorRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        AvaliacaoColaborador avaliacao = optional.get();

        if (resumo != null) {
            avaliacao.setResumo(resumo);
        }

        if (competenciaId != null) {
            Competencia competencia = competenciaRepository.findById(competenciaId)
                    .orElseThrow(() -> new IllegalArgumentException("Competência não encontrada com ID: " + competenciaId));
            avaliacao.setCompetencia(competencia);
        }

        if (status != null) {
            avaliacao.setStatus(status);
        }

        if (publico != null) {
            avaliacao.setPublico(publico);
        }

        AvaliacaoColaborador atualizada = avaliacaoColaboradorRepository.save(avaliacao);
        return Optional.of(mapToDTO(atualizada));
    }

    public List<AvaliacaoColaboradorDTO> listarPorColaborador(Long colaboradorId) {
        List<AvaliacaoColaborador> avaliacoes = avaliacaoColaboradorRepository.findByAvaliadoIdAndStatusTrue(colaboradorId);
        return avaliacoes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AvaliacaoColaboradorDTO> listarTodas() {
        List<AvaliacaoColaborador> avaliacoes = avaliacaoColaboradorRepository.findAll();
        return avaliacoes.stream()
                .filter(a -> Boolean.TRUE.equals(a.getStatus()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AvaliacaoColaboradorDTO mapToDTO(AvaliacaoColaborador avaliacao) {
        return AvaliacaoColaboradorDTO.builder()
                .id(avaliacao.getId())
                .avaliadorId(avaliacao.getAvaliador() != null ? avaliacao.getAvaliador().getId() : null)
                .avaliadorNome(avaliacao.getAvaliador() != null 
                        ? (avaliacao.getAvaliador().getNome() + " " + avaliacao.getAvaliador().getSobrenome()).trim() 
                        : null)
                .avaliadoId(avaliacao.getAvaliado() != null ? avaliacao.getAvaliado().getId() : null)
                .avaliadoNome(avaliacao.getAvaliado() != null 
                        ? (avaliacao.getAvaliado().getNome() + " " + avaliacao.getAvaliado().getSobrenome()).trim() 
                        : null)
                .resumo(avaliacao.getResumo())
                .competenciaId(avaliacao.getCompetencia() != null ? avaliacao.getCompetencia().getId() : null)
                .competenciaNome(avaliacao.getCompetencia() != null ? avaliacao.getCompetencia().getNome() : null)
                .status(avaliacao.getStatus())
                .publico(avaliacao.getPublico())
                .build();
    }
}

