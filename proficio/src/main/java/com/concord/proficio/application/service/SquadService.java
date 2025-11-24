package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.domain.entities.ColaboradorSquad;
import com.concord.proficio.domain.entities.Squad;
import com.concord.proficio.application.dto.SquadDTO;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.ColaboradorSquadRepository;
import com.concord.proficio.infra.repositories.SquadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SquadService {

	private final SquadRepository squadRepository;
	private final ColaboradorRepository colaboradorRepository;
	private final ColaboradorSquadRepository colaboradorSquadRepository;

	public SquadService(SquadRepository squadRepository,
	                    ColaboradorRepository colaboradorRepository,
	                    ColaboradorSquadRepository colaboradorSquadRepository) {
		this.squadRepository = squadRepository;
		this.colaboradorRepository = colaboradorRepository;
		this.colaboradorSquadRepository = colaboradorSquadRepository;
	}

	public List<Squad> buscar(String q, String status) {
		String query = (q == null || q.isBlank()) ? null : q.toLowerCase();
		List<Squad> base;
		if ("all".equalsIgnoreCase(status)) {
			base = squadRepository.findAll();
		} else if ("inactive".equalsIgnoreCase(status)) {
			base = squadRepository.findAll().stream()
					.filter(s -> Boolean.FALSE.equals(s.getStatus()))
					.toList();
		} else {
			base = squadRepository.findByStatusTrue();
		}
		if (query == null) return base;
		return base.stream()
				.filter(s -> s.getNome() != null && s.getNome().toLowerCase().contains(query))
				.toList();
	}

	public Optional<Squad> buscarPorId(Long id) {
		return squadRepository.findById(id);
	}

	public Optional<Squad> buscarPorNome(String nome) {
		return squadRepository.findByNome(nome);
	}

	@Transactional
	public Squad criar(Squad squad, Long liderId) {
		if (liderId == null) throw new IllegalArgumentException("Líder é obrigatório");
		Colaborador lider = colaboradorRepository.findById(liderId)
				.orElseThrow(() -> new IllegalArgumentException("Colaborador (líder) não encontrado"));
		squad.setLider(lider);
		if (squad.getStatus() == null) squad.setStatus(true);
		Squad salvo = squadRepository.save(squad);  
		ColaboradorSquad cs = colaboradorSquadRepository.findByColaboradorIdAndSquadId(lider.getId(), salvo.getId())
				.orElseGet(() -> {
					ColaboradorSquad novo = new ColaboradorSquad();
					novo.setColaborador(lider);
					novo.setSquad(salvo);
					return novo;
				});
		cs.setStatus(true);
		colaboradorSquadRepository.save(cs);
		return salvo;
	}

	@Transactional
	public Optional<Squad> atualizar(Long id, String nome, String descricao, Long liderId) {
		Optional<Squad> optional = squadRepository.findById(id);
		if (optional.isEmpty()) return Optional.empty();
		Squad existente = optional.get();
		existente.setNome(nome);
		existente.setDescricao(descricao);
		if (liderId != null) {
			colaboradorRepository.findById(liderId).ifPresent(existente::setLider);
		}
		return Optional.of(squadRepository.save(existente));
	}

	@Transactional
	public boolean desativar(Long id) {
		Optional<Squad> optional = squadRepository.findById(id);
		if (optional.isEmpty()) return false;
		Squad squad = optional.get();
		squad.setStatus(false);
		squadRepository.save(squad);
		return true;
	}

	public List<SquadDTO> listarDTO(String q, String status) {
		return buscar(q, status).stream()
				.map(s -> SquadDTO.builder()
						.id(s.getId())
						.nome(s.getNome())
						.descricao(s.getDescricao())
						.status(s.getStatus())
						.liderId(s.getLider() != null ? s.getLider().getId() : null)
						.membrosCount(s.getId() != null ? contarMembrosAtivos(s.getId()) : 0)
						.build())
				.toList();
	}

	public long contarMembrosAtivos(Long squadId) {
		return colaboradorSquadRepository.countBySquadIdAndStatusTrue(squadId);
	}

	public List<Colaborador> listarMembrosAtivos(Long squadId) {
		return colaboradorSquadRepository.findBySquadIdAndStatusTrue(squadId).stream()
				.map(ColaboradorSquad::getColaborador)
				.toList();
	}

	@Transactional
	public boolean adicionarMembro(Long squadId, Long colaboradorId) {
		Squad squad = squadRepository.findById(squadId).orElse(null);
		Colaborador colaborador = colaboradorRepository.findById(colaboradorId).orElse(null);
		if (squad == null || colaborador == null) return false;
		Optional<ColaboradorSquad> existing = colaboradorSquadRepository.findByColaboradorIdAndSquadId(colaboradorId, squadId);
		if (existing.isPresent()) {
			ColaboradorSquad cs = existing.get();
			cs.setStatus(true);
			colaboradorSquadRepository.save(cs);
			return true;
		}
		ColaboradorSquad cs = new ColaboradorSquad();
		cs.setColaborador(colaborador);
		cs.setSquad(squad);
		cs.setStatus(true);
		colaboradorSquadRepository.save(cs);
		return true;
	}

	@Transactional
	public boolean removerMembro(Long squadId, Long colaboradorId) {
		Optional<ColaboradorSquad> optional = colaboradorSquadRepository.findByColaboradorIdAndSquadId(colaboradorId, squadId);
		if (optional.isEmpty()) return false;
		ColaboradorSquad cs = optional.get();
		cs.setStatus(false);
		colaboradorSquadRepository.save(cs);
		return true;
	}
}


