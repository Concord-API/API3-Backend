package com.concord.proficio.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetenciaDTO {
    private Long id;
    private String nome;
    private Byte tipo;
    private Integer proeficiencia;
    private Integer ordem;
}


