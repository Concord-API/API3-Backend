package com.concord.proficio.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import com.concord.proficio.domain.enums.GeneroEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colaborador")
@EntityListeners(AuditingEntityListener.class)
public class Colaborador implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_colaborador")
	private Long id;

	@Column(name = "cpf", length = 15, unique = true)
	private String cpf;

	@Column(name = "data_nasci")
	private LocalDate dataNascimento;

	@Column(name = "nome", nullable = false, length = 50)
	private String nome;

	@Column(name = "sobrenome", nullable = false, length = 50)
	private String sobrenome;

	@Column(name = "email", nullable = false, unique = true, length = 256)
	private String email;

	@Column(name = "senha", nullable = false, length = 256)
	private String senha;

    @Enumerated(EnumType.ORDINAL)
	@Column(name = "genero", nullable = false)
	private GeneroEnum genero;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 50)
	private ColaboradorRoleEnum role;

	@Column(name = "status", length = 1)
	private Boolean status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cargo", nullable = false)
	private Cargo cargo;

	@OneToMany(mappedBy = "gestor")
	private List<Equipe> equipes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_equipe", nullable = false)
	private Equipe equipe;

    @Lob
    @Column(name = "avatar", columnDefinition = "LONGBLOB")
	private byte[] avatar;

    @Lob
    @Column(name = "capa", columnDefinition = "LONGBLOB")
	private byte[] capa;

	@CreatedDate
	@Column(name = "criado_em", nullable = false)
	private LocalDateTime criadoEm;


	@LastModifiedDate
	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
