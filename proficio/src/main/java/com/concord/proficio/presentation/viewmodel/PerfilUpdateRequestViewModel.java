package com.concord.proficio.presentation.viewmodel;

import com.concord.proficio.domain.enums.GeneroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUpdateRequestViewModel {
    private String nome;
    private String sobrenome;
    private String email;
    private LocalDate dataNascimento;
    private GeneroEnum genero;
    private byte[] avatar;
    private byte[] capa;
}
