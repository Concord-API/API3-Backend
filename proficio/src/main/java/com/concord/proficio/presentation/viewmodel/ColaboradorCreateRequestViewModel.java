package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ColaboradorCreateRequestViewModel {
    @NotBlank
    private String nome;
    @NotBlank
    private String sobrenome;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String senha;

    private String genero;

    @NotNull
    private Long idEquipe;
    @NotNull
    private Long idCargo;

    private Boolean status;

    private LocalDate dataNascimento;
}


