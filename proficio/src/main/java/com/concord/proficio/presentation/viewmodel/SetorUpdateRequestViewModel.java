package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SetorUpdateRequestViewModel {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50)
    private String nome;

    @Size(max = 100)
    private String descricao;

    private Long diretorId;
}


