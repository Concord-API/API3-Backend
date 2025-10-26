package com.concord.proficio.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipeUpdateDTO {
    private String nome;
    private Long gestorId;
}