package br.com.fintracker.dto.categoria;

import java.math.BigDecimal;

public record DadosAtualizacaoCategoria(
        String nomeCategoria,
        BigDecimal cota,
        Boolean isAtivo
) {
}
