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
        return competenciaRepository.findAll();
    }

    @Transactional
    public Competencia criar(Competencia competencia) {
        return competenciaRepository.save(competencia);
    }
}