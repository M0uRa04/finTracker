package br.com.fintracker.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transacao {
    private Long id;
    private BigDecimal valor;
    private LocalDateTime data;
    private Categoria categoria;
    private String descricao;

    private TipoTransacao tipoTransacao;
    //private Usuario usuario;

    public Transacao () {};

    public Transacao(Long id, BigDecimal valor, LocalDateTime data, Categoria categoria, String descricao, TipoTransacao tipoTransacao) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.tipoTransacao = tipoTransacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id) && Objects.equals(valor, transacao.valor) && Objects.equals(data, transacao.data) && Objects.equals(categoria, transacao.categoria) && Objects.equals(descricao, transacao.descricao) && tipoTransacao == transacao.tipoTransacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, data, categoria, descricao, tipoTransacao);
    }

    public void registrar () {};

    public void atualizarSaldo() {};
}
