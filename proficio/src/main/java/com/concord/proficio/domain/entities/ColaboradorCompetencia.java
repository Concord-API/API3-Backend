package com.concord.proficio.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colaborador_competencia")
public class ColaboradorCompetencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_colaborador", nullable = false)
	private com.concord.proficio.domain.entities.Colaborador colaborador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_competencia", nullable = false)
	private Competencia competencia;

	@Column(name = "proeficiencia", nullable = false)
	private Integer proeficiencia;

	@Column(name = "ordem")
	private Integer ordem;

	@Lob
	@Column(name = "certificado")
	private byte[] certificado;
}
