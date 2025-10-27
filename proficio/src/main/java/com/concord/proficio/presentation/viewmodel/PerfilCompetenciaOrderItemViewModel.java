package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilCompetenciaOrderItemViewModel {
    private Long id;
    private Long id_competencia;
    private Integer ordem;
}


