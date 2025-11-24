package com.concord.proficio.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GeneroEnum {
    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private final String value;

    GeneroEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getGenero() {
        return this.value;
    }
}