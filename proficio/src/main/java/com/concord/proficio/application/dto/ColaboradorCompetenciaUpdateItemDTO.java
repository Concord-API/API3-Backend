package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorCompetenciaUpdateItemDTO {
    private Long competenciaId;
    private Integer proeficiencia;
    private Integer ordem;
    private String certificado;
}


