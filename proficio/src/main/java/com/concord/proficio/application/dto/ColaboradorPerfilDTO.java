package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorPerfilDTO {
    private Long id;
    private String nome;
    private String sobrenome;
    private String email;
    private Boolean status;
    private String role;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private byte[] avatar;
    private byte[] capa;
    private Cargo cargo;
    private Equipe equipe;
    private List<ColaboradorCompetenciaFull> competencias;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cargo {
        private Long id;
        private String nome;
        private String descricao;
        private Boolean status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setor {
        private Long id;
        private String nome;
        private String descricao;
        private Boolean status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Equipe {
        private Long id;
        private String nome;
        private Boolean status;
        private Setor setor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetenciaItem {
        private Long id;
        private String nome;
        private Byte tipo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColaboradorCompetenciaFull {
        private Long id;
        private Long colaboradorId;
        private Long competenciaId;
        private Integer proeficiencia;
        private Integer ordem;
        private CompetenciaItem competencia;
    }
}


