package com.yanmaia12.centavos.dtos;

import com.yanmaia12.centavos.enums.Categoria;
import com.yanmaia12.centavos.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(Long id,
                                   BigDecimal valor,
                                   String descricao,
                                   LocalDateTime data,
                                   TipoTransacao tipo,
                                   Categoria categoria,
                                   Long metaId) {
}
