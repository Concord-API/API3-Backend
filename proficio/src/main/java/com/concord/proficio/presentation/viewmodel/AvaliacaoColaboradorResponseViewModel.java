package com.concord.proficio.presentation.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoColaboradorResponseViewModel {
    private Long id;
    private Long avaliadorId;
    private String avaliadorNome;
    private Long avaliadoId;
    private String avaliadoNome;
    private String resumo;
    private Integer nota;
    private Long competenciaId;
    private String competenciaNome;
    private Boolean status;
    private Boolean publico;
    
    @JsonProperty("created_at")
    private LocalDateTime criadoEm;
    
    @JsonProperty("updated_at")
    private LocalDateTime atualizadoEm;
}

