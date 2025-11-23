package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Competencia;
import com.concord.proficio.infra.repositories.CompetenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompetenciaService {

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    public List<Competencia> listarTodos() {
        return competenciaRepository.findByStatusTrue();
    }

    @Transactional
    public boolean desativar(Long id) {
        return competenciaRepository.findById(id)
                .map(c -> {
                    c.setStatus(false);
                    competenciaRepository.save(c);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public Competencia criar(Competencia competencia) {
        competencia.setStatus(true);
        return competenciaRepository.save(competencia);
    }

    @Transactional
    public boolean aprovar(Long id) {
        return competenciaRepository.findById(id)
                .map(c -> {
                    c.setAprovada(true);
                    competenciaRepository.save(c);
                    return true;
                }).orElse(false);
    }
}