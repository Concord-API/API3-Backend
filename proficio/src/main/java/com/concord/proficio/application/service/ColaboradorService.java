package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import com.concord.proficio.presentation.dto.ColaboradorResponseDTO;
import com.concord.proficio.presentation.dto.CompetenciaDTO;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorCompetenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final ColaboradorCompetenciaRepository colaboradorCompetenciaRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository,
                              ColaboradorCompetenciaRepository colaboradorCompetenciaRepository) {
        this.colaboradorRepository = colaboradorRepository;
        this.colaboradorCompetenciaRepository = colaboradorCompetenciaRepository;
    }

    // ---------------------------
    // CRUD básico do colaborador
    // ---------------------------
    public List<Colaborador> listarTodos() {
        return colaboradorRepository.findAll();
    }

    public Optional<Colaborador> buscarPorId(Long id) {
        return colaboradorRepository.findById(id);
    }

    public Colaborador salvar(Colaborador colaborador) {
        return colaboradorRepository.save(colaborador);
    }

    public void deletar(Long id) {
        colaboradorRepository.deleteById(id);
    }

    // ---------------------------
    // Atualização de arquivos
    // ---------------------------
    public void atualizarFoto(Long id, byte[] foto) {
        colaboradorRepository.findById(id).ifPresent(c -> {
            c.setAvatar(foto);
            colaboradorRepository.save(c);
        });
    }

    public void atualizarCapa(Long id, byte[] capa) {
        colaboradorRepository.findById(id).ifPresent(c -> {
            c.setCapa(capa);
            colaboradorRepository.save(c);
        });
    }

    // ---------------------------
    // Competências do colaborador
    // ---------------------------
    public List<ColaboradorCompetencia> listarCompetencias(Long colaboradorId) {
        return colaboradorCompetenciaRepository.findByColaboradorId(colaboradorId);
    }

    public ColaboradorCompetencia adicionarCompetencia(ColaboradorCompetencia competencia) {
        return colaboradorCompetenciaRepository.save(competencia);
    }

    public void removerCompetencia(Long competenciaId) {
        colaboradorCompetenciaRepository.deleteById(competenciaId);
    }

    // ---------------------------
    // DTO mapping
    // ---------------------------
    public Optional<ColaboradorResponseDTO> buscarDetalhadoComCompetencias(Long id) {
        Optional<Colaborador> optional = colaboradorRepository.findById(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        Colaborador colaborador = optional.get();

        List<ColaboradorCompetencia> competencias = colaboradorCompetenciaRepository.findByColaboradorId(colaborador.getId());
        List<CompetenciaDTO> competenciaDTOs = competencias.stream()
                .map(cc -> CompetenciaDTO.builder()
                        .id(cc.getCompetencia().getId())
                        .nome(cc.getCompetencia().getNome())
                        .tipo(cc.getCompetencia().getTipo())
                        .proeficiencia(cc.getProeficiencia())
                        .ordem(cc.getOrdem())
                        .build())
                .collect(Collectors.toList());

        ColaboradorResponseDTO dto = ColaboradorResponseDTO.builder()
                .id(colaborador.getId())
                .nome(colaborador.getNome())
                .sobrenome(colaborador.getSobrenome())
                .email(colaborador.getEmail())
                .cpf(colaborador.getCpf())
                .dataNascimento(colaborador.getDataNascimento())
                .status(colaborador.getStatus())
                .competencias(competenciaDTOs)
                .build();

        return Optional.of(dto);
    }
}
