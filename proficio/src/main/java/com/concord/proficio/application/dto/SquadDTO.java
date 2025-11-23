package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SquadDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Boolean status;
    private Long liderId;
    private Long membrosCount;
}





