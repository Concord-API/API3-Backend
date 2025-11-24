package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorCompetenciaDTO {
    private Long id;
    private Long idCompetencia;
    private String nome;
    private Byte tipo;
    private Integer proeficiencia;
    private Integer ordem;
    private Boolean certificado;
}


