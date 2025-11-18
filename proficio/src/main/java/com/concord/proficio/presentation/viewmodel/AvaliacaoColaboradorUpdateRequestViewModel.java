package com.concord.proficio.presentation.viewmodel;

import lombok.Data;

@Data
public class AvaliacaoColaboradorUpdateRequestViewModel {
    private String resumo;
    private Long competenciaId;
    private Boolean status;
    private Boolean publico;
}

