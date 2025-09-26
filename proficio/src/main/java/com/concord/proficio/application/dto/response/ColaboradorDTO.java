package com.concord.proficio.application.dto.response;

import com.concord.proficio.application.dto.CompetenciaDTO;
import com.concord.proficio.domain.entities.Colaborador;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ColaboradorDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private Boolean status;
    private List<CompetenciaDTO> competencias;

    public static ColaboradorDTO fromEntity(Colaborador colaborador, List<CompetenciaDTO> competencias) {
        ColaboradorDTO dto = new ColaboradorDTO();
        dto.setId(colaborador.getId());
        dto.setNome(colaborador.getNome());
        dto.setSobrenome(colaborador.getSobrenome());
        dto.setEmail(colaborador.getEmail());
        dto.setCpf(colaborador.getCpf());
        dto.setDataNascimento(colaborador.getDataNascimento());
        dto.setStatus(colaborador.getStatus());
        dto.setCompetencias(competencias);
        return dto;
    }
}
