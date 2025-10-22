package com.concord.proficio.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "competencia")
public class Competencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_competencia")
	private Long id;

	@Column(name = "nome", nullable = false, length = 45)
	private String nome;

	@Column(name = "tipo", nullable = false)
	private Byte tipo;
	
	@Column(name = "status", length = 1)
	private Boolean status;
}
