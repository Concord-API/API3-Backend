package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoColaboradorCreateRequestViewModel {
    @NotNull(message = "ID do avaliador é obrigatório")
    private Long avaliadorId;

    @NotNull(message = "ID do avaliado é obrigatório")
    private Long avaliadoId;

    private String resumo;

    private Integer nota;

    private Long competenciaId;

    private Boolean status;

    @NotNull(message = "Campo público é obrigatório")
    private Boolean publico;
}

