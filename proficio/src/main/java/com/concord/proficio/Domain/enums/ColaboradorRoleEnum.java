package com.concord.proficio.domain.enums;

public enum ColaboradorRoleEnum {
    COLABORADOR,
    GESTOR,
    DIRETOR;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
