package com.yanmaia12.centavos.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ResumoMensalDTO(BigDecimal receita,
                              BigDecimal despesa,
                              List<TransacaoResponseDTO> transacoes) {
}
