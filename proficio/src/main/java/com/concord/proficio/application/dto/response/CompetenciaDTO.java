package com.concord.proficio.application.dto;

import com.concord.proficio.domain.entities.ColaboradorCompetencia;
import lombok.Data;

@Data
public class CompetenciaDTO {
    private Long id;
    private String nome;
    private Byte tipo;
    private Integer proeficiencia;
    private Integer ordem;

    public static CompetenciaDTO fromEntity(ColaboradorCompetencia cc) {
        CompetenciaDTO dto = new CompetenciaDTO();
        dto.setId(cc.getCompetencia().getId());
        dto.setNome(cc.getCompetencia().getNome());
        dto.setTipo(cc.getCompetencia().getTipo());
        dto.setProeficiencia(cc.getProeficiencia());
        dto.setOrdem(cc.getOrdem());
        return dto;
    }
}
