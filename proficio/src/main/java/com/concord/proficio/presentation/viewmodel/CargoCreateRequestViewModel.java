package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CargoCreateRequestViewModel {
	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 50)
	private String nome;

	@Size(max = 100)
	private String descricao;

	@NotBlank(message = "Role é obrigatória")
	@Size(max = 50)
	private String role;

	@NotNull(message = "Setor é obrigatório")
	private Long setorId;
}


