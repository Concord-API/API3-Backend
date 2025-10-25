package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EquipeResponseViewModel {
    private Long id;
    private String nome;
    private Boolean status;
    private Long gestorId;
    private Long setorId;
}