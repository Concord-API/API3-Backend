package com.concord.proficio.domain.entities;

import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "cargo")
public class Cargo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cargo")
	private Long id;

	@Column(name = "nome_cargo", nullable = false, length = 50)
	private String nome;

	@Column(name = "desc_cargo", length = 100)
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 50)
	private ColaboradorRoleEnum role;

	@Column(name = "status", length = 1)
	private Boolean status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_setor", nullable = false)
	private Setor setor;
}
