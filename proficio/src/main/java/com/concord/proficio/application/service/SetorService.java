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
public class SetorService {

    private final SetorRepository setorRepository;
    private final EquipeRepository equipeRepository;
    private final ColaboradorRepository colaboradorRepository;

    public SetorService(SetorRepository setorRepository,
                        EquipeRepository equipeRepository,
                        ColaboradorRepository colaboradorRepository) {
        this.setorRepository = setorRepository;
        this.equipeRepository = equipeRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

	public List<Setor> listarTodos() {
        return setorRepository.findByStatusTrue();
	}

	public Optional<Setor> buscarPorNome(String nome) {
		return setorRepository.findByNome(nome);
	}

	public Optional<Setor> buscarPorId(Long id) {
		return setorRepository.findById(id);
	}


	@Transactional
	public Setor criar(Setor setor, Long diretorId) {
		if (diretorId != null) {
			colaboradorRepository.findById(diretorId).ifPresent(setor::setDiretor);
		}
		return setorRepository.save(setor);
	}


	@Transactional
	public Optional<Setor> atualizar(Long id, String nome, String descricao, Long diretorId) {
		Optional<Setor> optional = setorRepository.findById(id);
		if (optional.isEmpty()) return Optional.empty();
		Setor existente = optional.get();
		existente.setNome(nome);
		existente.setDescricao(descricao);
		if (diretorId == null) {
			existente.setDiretor(null);
		} else {
			colaboradorRepository.findById(diretorId).ifPresent(existente::setDiretor);
		}
		return Optional.of(setorRepository.save(existente));
	}

	@Transactional
	public boolean desativar(Long id) {
		Optional<Setor> optional = setorRepository.findById(id);
		if (optional.isEmpty()) return false;
		Setor setor = optional.get();
		setor.setStatus(false);
		setorRepository.save(setor);
		return true;
	}

    public List<Setor> buscar(String q) {
        return setorRepository.searchActive(q == null || q.isBlank() ? null : q);
    }

    public long contarEquipesAtivasDoSetor(Long setorId) {
        return equipeRepository.countBySetorIdAndStatusTrue(setorId);
    }

    public long contarColaboradoresAtivosDoSetor(Long setorId) {
        return colaboradorRepository.countByEquipeSetorIdAndStatusTrue(setorId);
    }

    public List<Equipe> listarEquipesAtivasDoSetor(Long setorId) {
        return equipeRepository.findBySetorIdAndStatusTrue(setorId);
    }

    public List<Colaborador> listarColaboradoresAtivosDoSetor(Long setorId) {
        return colaboradorRepository.findByEquipeSetorIdAndStatusTrue(setorId);
    }
}


