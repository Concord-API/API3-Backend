package com.concord.proficio.application.dto;

import com.concord.proficio.domain.enums.GeneroEnum;
import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorListDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private ColaboradorRoleEnum role;
    private String nomeCargo;
    private Long idEquipe;
    private Long idSetor;
    private byte[] avatar;
    private byte[] capa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
