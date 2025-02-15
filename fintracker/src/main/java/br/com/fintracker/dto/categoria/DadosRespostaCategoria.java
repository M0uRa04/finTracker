package br.com.fintracker.dto.categoria;

import br.com.fintracker.model.categoria.Categoria;

import java.math.BigDecimal;

public record DadosRespostaCategoria (
        String nomeCategoria,
        BigDecimal cota
) {
    public DadosRespostaCategoria(Categoria c) {
        this(c.getNomeCategoria(), c.getCota());
    }
}
