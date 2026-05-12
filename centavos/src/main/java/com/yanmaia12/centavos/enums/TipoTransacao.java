package com.yanmaia12.centavos.enums;

public enum TipoTransacao {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String label;

    TipoTransacao(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
