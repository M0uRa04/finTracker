package br.com.fintracker.dto.categoria;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record DadosCadastroCategoria(
        @NotBlank
        String nomeCategoria,
        BigDecimal cota

) {
}
