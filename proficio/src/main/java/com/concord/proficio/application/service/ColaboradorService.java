package com.concord.proficio.application.service;

import com.concord.proficio.domain.entities.Colaborador;
import com.concord.proficio.infra.repositories.ColaboradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

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
}
