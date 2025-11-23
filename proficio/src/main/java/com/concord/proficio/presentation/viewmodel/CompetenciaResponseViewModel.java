package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CompetenciaResponseViewModel {
    private Long id;
    private String nome;
    private Byte tipo;
    private Boolean aprovada;
}


