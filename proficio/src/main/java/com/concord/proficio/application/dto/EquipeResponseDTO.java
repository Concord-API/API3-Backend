package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipeResponseDTO {
    private Long id;
    private String nome;
    private Boolean status;
    private Long gestorId;
    private Long setorId;
    private Long colaboradoresCount;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}