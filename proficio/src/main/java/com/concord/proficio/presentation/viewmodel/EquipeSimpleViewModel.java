package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipeSimpleViewModel {
    private Long id_equipe;
    private String nome_equipe;
    private Boolean status;
    private Long id_setor;
    private SetorSimpleViewModel setor;
}
