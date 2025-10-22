package com.concord.proficio.domain.enums;

public enum GeneroEnum {
    Masculino,
    Feminino;

    public String getGenero() {
        return this.name();
    }
}