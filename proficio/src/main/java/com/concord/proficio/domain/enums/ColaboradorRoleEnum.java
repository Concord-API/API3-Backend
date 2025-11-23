package com.concord.proficio.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColaboradorRoleEnum {
    COLABORADOR("Colaborador"),
    GESTOR("Gestor"),
    DIRETOR("Diretor");

    private final String value;

    ColaboradorRoleEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getRole() {
        return "Role_" + this.value;
    }
}
