package br.com.fintracker.dto.transacao;

import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.transacao.TipoTransacao;
import br.com.fintracker.model.transacao.Transacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosRespostaTransacao(
        Long usuarioId,
        Long transacaoId,
        TipoTransacao transacao,
        DadosRespostaCategoria categoria,
        LocalDate date,
        BigDecimal valor,
        String descricao
) {
    public DadosRespostaTransacao (Transacao transacao) {
        this(transacao.getUsuario().getId(),transacao.getId(),transacao.getTipoTransacao(), new DadosRespostaCategoria(transacao.getCategoria()), transacao.getDataTransacao(), transacao.getValor(), transacao.getDescricao());
    }
}
