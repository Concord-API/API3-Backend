package com.concord.proficio.presentation.dto;

import java.util.List;

public class ColaboradorCompetenciaUpdateDTO {
    private List<ColaboradorCompetenciaUpdateItemDTO> items;

    public ColaboradorCompetenciaUpdateDTO() {}

    public ColaboradorCompetenciaUpdateDTO(List<ColaboradorCompetenciaUpdateItemDTO> items) {
        this.items = items;
    }

    public List<ColaboradorCompetenciaUpdateItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ColaboradorCompetenciaUpdateItemDTO> items) {
        this.items = items;
    }
}