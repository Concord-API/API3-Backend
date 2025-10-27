package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ColaboradorCompetenciaUpdateItemRequestViewModel {
    @NotNull(message = "competenciaId é obrigatório")
    private Long competenciaId;

    @Min(value = 0, message = "proeficiencia deve ser maior ou igual a 0")
    private Integer proeficiencia;

    private Integer ordem;
}


