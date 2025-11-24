package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorCompetenciaFullViewModel {
    private Long id;
    private Long id_colaborador;
    private Long id_competencia;
    private Integer proeficiencia;
    private Integer ordem;
    private Boolean certificado;
    private CompetenciaItemViewModel competencia;
}
