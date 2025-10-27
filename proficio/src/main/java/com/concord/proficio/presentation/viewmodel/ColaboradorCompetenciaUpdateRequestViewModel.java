package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ColaboradorCompetenciaUpdateRequestViewModel {
    @NotEmpty(message = "items não pode ser vazio")
    @Valid
    private List<ColaboradorCompetenciaUpdateItemRequestViewModel> items;
}


