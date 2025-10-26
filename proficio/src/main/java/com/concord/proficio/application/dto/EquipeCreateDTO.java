package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipeCreateDTO {
    private String nome;
    private Long setorId;
    private Long gestorId;
}