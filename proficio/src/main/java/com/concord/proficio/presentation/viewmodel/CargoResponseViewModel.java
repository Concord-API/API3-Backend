package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CargoResponseViewModel {
    private Long id_cargo;
    private String nome_cargo;
    private String desc_cargo;
    private String role;
    private Boolean status;
    private Long id_setor;
}


