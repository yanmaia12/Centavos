package com.yanmaia12.centavos.dtos;

import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDTO(@NotNull @Positive BigDecimal valor,
         @NotBlank String descricao,
         @NotNull LocalDateTime data,
         @NotNull TipoTransacao tipo,
         @NotNull Categoria categoria,
         Long metaId) {
}
