package br.com.fintracker.dto.transacao;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.transacao.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroTransacao(

        @NotBlank
        TipoTransacao transacao,
        @NotBlank
        Categoria categoria,
        @NotNull
        LocalDate date,
        @NotNull
        BigDecimal valor,
        String descricao
) {}
