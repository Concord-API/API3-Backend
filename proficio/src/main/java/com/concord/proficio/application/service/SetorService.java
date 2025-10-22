package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Setor;
import com.concord.proficio.infra.repositories.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SetorService {

	private final SetorRepository setorRepository;

	public SetorService(SetorRepository setorRepository) {
		this.setorRepository = setorRepository;
	}

	public List<Setor> listarTodos() {
		return setorRepository.findAll();
	}

	public Optional<Setor> buscarPorNome(String nome) {
		return setorRepository.findByNome(nome);
	}

	@Transactional
	public Setor criar(Setor setor) {
		return setorRepository.save(setor);
	}
}


