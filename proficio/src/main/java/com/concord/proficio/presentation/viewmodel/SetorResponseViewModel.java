package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SetorResponseViewModel {
	private Long id;
	private String nome;
	private String descricao;
	private Boolean status;
	private Long equipesCount;
	private Long colaboradoresCount;
}

