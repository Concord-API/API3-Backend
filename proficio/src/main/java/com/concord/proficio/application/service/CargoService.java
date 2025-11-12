package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Cargo;
import com.concord.proficio.domain.entities.Setor;
import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import com.concord.proficio.infra.repositories.CargoRepository;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import com.concord.proficio.infra.repositories.SetorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

	private final CargoRepository cargoRepository;
	private final SetorRepository setorRepository;
	private final ColaboradorRepository colaboradorRepository;

	public CargoService(CargoRepository cargoRepository,
	                    SetorRepository setorRepository,
	                    ColaboradorRepository colaboradorRepository) {
		this.cargoRepository = cargoRepository;
		this.setorRepository = setorRepository;
		this.colaboradorRepository = colaboradorRepository;
	}

	public List<Cargo> listarAtivos() {
		return cargoRepository.findByStatusTrue();
	}

	public List<Cargo> buscar(String q) {
		return buscar(q, "active");
	}

	public List<Cargo> buscar(String q, String status) {
		String query = (q == null || q.isBlank()) ? null : q.toLowerCase();
		List<Cargo> base;
		if ("all".equalsIgnoreCase(status)) {
			base = cargoRepository.findAll();
		} else if ("inactive".equalsIgnoreCase(status)) {
			base = cargoRepository.findAll().stream()
					.filter(c -> Boolean.FALSE.equals(c.getStatus()))
					.toList();
		} else {
			base = cargoRepository.findByStatusTrue();
		}
		if (query == null) return base;
		return base.stream()
				.filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(query))
				.toList();
	}

	public Optional<Cargo> buscarPorNome(String nome) {
		return cargoRepository.findByNome(nome);
	}

	public Optional<Cargo> buscarPorId(Long id) {
		return cargoRepository.findById(id);
	}

	@Transactional
	public Cargo criar(Cargo cargo, Long setorId, String role) {
		Setor setor = setorRepository.findById(setorId)
				.orElseThrow(() -> new IllegalArgumentException("Setor não encontrado com ID: " + setorId));
		cargo.setSetor(setor);
		if (role != null) {
			try {
				cargo.setRole(ColaboradorRoleEnum.valueOf(role));
			} catch (Exception ignored) {
				throw new IllegalArgumentException("Role inválida: " + role);
			}
		}
		cargo.setStatus(true);
		return cargoRepository.save(cargo);
	}

	@Transactional
	public Optional<Cargo> atualizar(Long id, String nome, String descricao, Long setorId, String role) {
		Optional<Cargo> optional = cargoRepository.findById(id);
		if (optional.isEmpty()) return Optional.empty();
		Cargo existente = optional.get();
		existente.setNome(nome);
		existente.setDescricao(descricao);
		if (setorId != null) {
			setorRepository.findById(setorId).ifPresent(existente::setSetor);
		}
		if (role != null && !role.isBlank()) {
			try {
				existente.setRole(ColaboradorRoleEnum.valueOf(role));
			} catch (Exception ignored) {
				throw new IllegalArgumentException("Role inválida: " + role);
			}
		}
		Cargo salvo = cargoRepository.save(existente);
		return Optional.of(salvo);
	}

	@Transactional
	public boolean desativar(Long id) {
		Optional<Cargo> optional = cargoRepository.findById(id);
		if (optional.isEmpty()) return false;
		Cargo cargo = optional.get();
		cargo.setStatus(false);
		cargoRepository.save(cargo);
		return true;
	}

	public long contarColaboradoresAtivosDoCargo(Long cargoId) {
		return colaboradorRepository.countByCargoIdAndStatusTrue(cargoId);
	}
}


