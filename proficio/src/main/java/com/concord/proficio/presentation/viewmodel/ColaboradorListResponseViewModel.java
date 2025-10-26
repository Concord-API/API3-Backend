package com.concord.proficio.presentation.viewmodel;

import com.concord.proficio.domain.enums.GeneroEnum;
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
public class ColaboradorListResponseViewModel {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private String role;
    private String cargoNome;
    private byte[] avatar;
    private byte[] capa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
