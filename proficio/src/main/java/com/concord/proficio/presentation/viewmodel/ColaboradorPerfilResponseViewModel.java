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
    private CargoVM cargo;

    private Long id_equipe;
    private EquipeVM equipe;

    private List<ColaboradorCompetenciaFullVM> competencias;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CargoVM {
        private Long id_cargo;
        private String nome_cargo;
        private String desc_cargo;
        private Boolean status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetorVM {
        private Long id_setor;
        private String nome_setor;
        private String desc_setor;
        private Boolean status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipeVM {
        private Long id_equipe;
        private String nome_equipe;
        private Boolean status;
        private Long id_setor;
        private SetorVM setor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetenciaItemVM {
        private Long id_competencia;
        private String nome;
        private Byte tipo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColaboradorCompetenciaFullVM {
        private Long id;
        private Long id_colaborador;
        private Long id_competencia;
        private Integer proeficiencia;
        private Integer ordem;
        private CompetenciaItemVM competencia;
    }
}


