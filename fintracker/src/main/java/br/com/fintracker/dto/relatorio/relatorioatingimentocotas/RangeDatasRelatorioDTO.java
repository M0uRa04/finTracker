package br.com.fintracker.dto.relatorio.relatorioatingimentocotas;

import java.time.LocalDate;

public record RangeDatasRelatorioDTO(

    LocalDate dataInicio,
    LocalDate dataFim
) {
    
}
