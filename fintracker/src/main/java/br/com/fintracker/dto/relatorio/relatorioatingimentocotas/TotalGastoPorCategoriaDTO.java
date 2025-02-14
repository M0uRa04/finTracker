package br.com.fintracker.dto.relatorio.relatorioatingimentocotas;

import br.com.fintracker.model.categoria.Categoria;

import java.math.BigDecimal;

public record TotalGastoPorCategoriaDTO(
        Categoria categoria,
        BigDecimal totalGasto
) {
}
