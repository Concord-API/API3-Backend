package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ColaboradorCompetenciaResponseViewModel {
    private Long id;
    private Long idCompetencia;
    private String nome;
    private Byte tipo;
    private Boolean aprovada;
    private Integer proeficiencia;
    private Integer ordem;
    private Boolean certificado;
}


