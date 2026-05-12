package com.yanmaia12.centavos.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String email,
                       @NotBlank String senha) {
}
