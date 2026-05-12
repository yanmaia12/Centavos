package com.yanmaia12.centavos.dtos;

import jakarta.validation.constraints.NotBlank;

public record CadastroDTO(@NotBlank String nome,
                          @NotBlank String email,
                          @NotBlank String senha,
                          @NotBlank String confirmarSenha,
                          @NotBlank String moeda) {
}
