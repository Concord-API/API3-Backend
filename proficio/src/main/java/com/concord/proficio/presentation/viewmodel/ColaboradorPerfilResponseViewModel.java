package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorPerfilResponseViewModel {
    private Long id_colaborador;
    private String nome;
    private String sobrenome;
    private String email;
    private Boolean status;
    private String role;
    private String data_nasci;
    private String criado_em;
    private String atualizado_em;
    private String avatar;
    private String capa;

    private Long id_cargo;
    private CargoResponseViewModel cargo;

    private Long id_equipe;
    private EquipeSimpleViewModel equipe;

    private List<ColaboradorCompetenciaFullViewModel> competencias;
}
