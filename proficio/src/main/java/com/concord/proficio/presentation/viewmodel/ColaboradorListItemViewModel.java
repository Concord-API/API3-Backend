package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ColaboradorListItemViewModel {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String cargo;
}
