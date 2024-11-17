package br.com.fintracker.dto;

import br.com.fintracker.model.Categoria;
import br.com.fintracker.model.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoDTO (

        @NotNull
        BigDecimal valor,
        @NotNull
        LocalDate date, // fazer validação de anos
        @NotBlank
        Categoria categoria,

        @NotBlank
        String descricao,
        @NotBlank
        TipoTransacao transacao

) {}
