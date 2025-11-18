package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoColaboradorResponseViewModel {
    private Long id;
    private Long avaliadorId;
    private String avaliadorNome;
    private Long avaliadoId;
    private String avaliadoNome;
    private String resumo;
    private Long competenciaId;
    private String competenciaNome;
    private Boolean status;
    private Boolean publico;
}

