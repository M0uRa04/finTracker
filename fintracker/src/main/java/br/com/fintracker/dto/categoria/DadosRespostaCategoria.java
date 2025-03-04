package br.com.fintracker.dto.categoria;

import br.com.fintracker.model.categoria.Categoria;

import java.math.BigDecimal;

public record DadosRespostaCategoria (
        Long usuarioId,
        Long categoriaId,
        String nomeCategoria,
        BigDecimal cota
) {
    public DadosRespostaCategoria(Categoria c) {
        this(c.getUsuario().getId(),c.getId(),c.getNomeCategoria(), c.getCota());
    }
}
