package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.Equipe;
import com.concord.proficio.domain.entities.Setor;
import com.concord.proficio.infra.repositories.SetorRepository;
import com.concord.proficio.infra.repositories.EquipeRepository;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final SetorRepository setorRepository;
    private final ColaboradorRepository colaboradorRepository;

    public EquipeService(EquipeRepository equipeRepository,
                        SetorRepository setorRepository,
                         ColaboradorRepository colaboradorRepository) {
        this.setorRepository = setorRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    public List<Equipe> listarTodos() {
        return equipeRepository.findByStatusTrue();
    }

    public Optional<Equipe> buscarPorNome(String nome) {
        return equipeRepository.findByNome(nome);
    }

    public Optional<Equipe> buscarPorId(Long id) {
        return equipeRepository.findById(id);
    }


    @Transactional
    public Equipe criar(Equipe equipe, Long setorId, Long gestorId) {
        Setor setor = setorRepository.findById(setorId)
                .orElseThrow(() -> new IllegalArgumentException("Setor não encontrado com ID: " + setorId));
        equipe.setSetor(setor);

        if (gestorId != null) {
            colaboradorRepository.findById(gestorId).ifPresent(equipe::setGestor);
        }
        equipe.setStatus(true);
        return equipeRepository.save(equipe);
    }


    @Transactional
    public Optional<Equipe> atualizar(Long id, String nome, Long gestorId) {
        Optional<Equipe> optional = equipeRepository.findById(id);
        if (optional.isEmpty()) return Optional.empty();
        Equipe existente = optional.get();
        existente.setNome(nome);
        if (gestorId == null) {
            existente.setGestor(null);
        } else {
            colaboradorRepository.findById(gestorId).ifPresent(existente::setGestor);
        }
        return Optional.of(equipeRepository.save(existente));
    }

    @Transactional
    public boolean desativar(Long id) {
        Optional<Equipe> optional = equipeRepository.findById(id);
        if (optional.isEmpty()) return false;
        Equipe equipe = optional.get();
        equipe.setStatus(false);
        equipeRepository.save(equipe);
        return true;
    }

    public List<Equipe> buscar(String q) {
        return equipeRepository.searchActive(q == null || q.isBlank() ? null : q);
    }

    public long contarColaboradoresAtivosDaEquipe(Long equipeId) {
        return colaboradorRepository.countByEquipeIdAndStatusTrue(equipeId);
    }

}


