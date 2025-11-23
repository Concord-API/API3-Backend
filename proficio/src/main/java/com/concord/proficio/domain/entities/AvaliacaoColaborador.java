package com.concord.proficio.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "avaliacao_colaborador")
public class AvaliacaoColaborador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_avaliacao")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avaliador", nullable = false)
	private Colaborador avaliador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avaliado", nullable = false)
	private Colaborador avaliado;

	@Lob
	@Column(name = "resumo", columnDefinition = "TEXT")
	private String resumo;

	@Column(name = "nota")
	private Integer nota;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_competencia", nullable = false)
	private Competencia competencia;

	@Column(name = "status", length = 1)
	private Boolean status;

	@Column(name = "publico", nullable = false)
	private Boolean publico;

	@CreatedDate
	@Column(name = "criado_em", updatable = false)
	private LocalDateTime criadoEm;

	@LastModifiedDate
	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;
}

