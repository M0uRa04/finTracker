package br.com.fintracker.dto.relatorio;

import java.math.BigDecimal;

public record TotalGastoPorCategoriaDTO (

    String nomeCategoria,
    
    BigDecimal totalGasto

) {}