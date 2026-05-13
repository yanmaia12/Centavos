package com.yanmaia12.centavos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaDto(@NotBlank String nome,
                      String descricao,
                      @NotNull @Positive BigDecimal valorFinal,
                      LocalDate data) {
}
