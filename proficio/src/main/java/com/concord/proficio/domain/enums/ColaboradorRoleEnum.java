package com.concord.proficio.domain.enums;

public enum ColaboradorRoleEnum {
    Colaborador,
    Gestor,
    Diretor;

    public String getRole() {
        return "Role_" + this.name();
    }
}
