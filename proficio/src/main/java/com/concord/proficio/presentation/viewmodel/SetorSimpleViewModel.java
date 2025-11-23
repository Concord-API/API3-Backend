package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetorSimpleViewModel {
    private Long id_setor;
    private String nome_setor;
    private String desc_setor;
    private Boolean status;
}
