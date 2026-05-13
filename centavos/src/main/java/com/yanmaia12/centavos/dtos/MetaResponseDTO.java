package com.yanmaia12.centavos.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaResponseDTO(long id,
                              String nome,
                              String descricao,
                              BigDecimal valorFinal,
                              BigDecimal valorAtual,
                              LocalDate data,
                              Boolean finalizada) {
}
