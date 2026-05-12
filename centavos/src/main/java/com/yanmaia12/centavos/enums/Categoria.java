package com.yanmaia12.centavos.enums;

public enum Categoria {
    // DESPESAS
    ALIMENTACAO("Alimentação"),
    TRANSPORTE("Transporte"),
    MORADIA("Moradia"),
    SAUDE("Saúde"),
    EDUCACAO("Educação"),
    ENTRETENIMENTO("Entretenimento"),
    COMPRAS("Compras"),
    UTILIDADES("Utilidades"),
    PESSOAL("Pessoal"),
    VIAGEM("Viagem"),
    COFRE("Cofre"),
    DESPESA_OUTROS("Outros"),

    // RECEITAS
    SALARIO("Salário"),
    FREELANCE("Freelance"),
    INVESTIMENTOS("Investimentos"),
    BONUS("Bônus"),
    PRESENTE("Presente"),
    RECEITA_OUTROS("Outros");

    private final String label;

    Categoria(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
