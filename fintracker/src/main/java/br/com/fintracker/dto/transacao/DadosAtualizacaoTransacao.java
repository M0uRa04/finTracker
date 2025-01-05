package br.com.fintracker.dto.transacao;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.transacao.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoTransacao(
        TipoTransacao tipoTransacao,
        Categoria categoria,
        LocalDate dataTransacao,
        BigDecimal valor,
        String descricao
) {
}
