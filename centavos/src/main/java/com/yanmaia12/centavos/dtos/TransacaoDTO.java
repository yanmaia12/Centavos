package com.yanmaia12.centavos.dtos;

import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDTO(@NotNull BigDecimal valor,
         @NotBlank String descricao,
         @NotNull LocalDateTime data,
         @NotNull TipoTransacao tipo,
         @NotNull Categoria categoria,
         @NotNull Long usuarioId) {
}
