package br.com.fintracker.dto.relatorio;

import java.math.BigDecimal;

public record TotalGastoPorCategoriaDTO (

    Categoria categoria,
    
    BigDecimal totalGasto

) {}