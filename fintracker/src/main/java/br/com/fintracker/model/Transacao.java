package br.com.fintracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table
@Entity
public class Transacao {
    private Long id;
    private BigDecimal valor;
    private LocalDateTime data;
    //private Categoria categoria;
    private String descricao;

    //private TipoTransacao tipoTransacao;
    //private Usuario usuario;

    public Transacao () {};

    public Transacao(Long id, BigDecimal valor, LocalDateTime data, String descricao) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(getId(), transacao.getId()) && Objects.equals(getValor(), transacao.getValor()) && Objects.equals(getData(), transacao.getData()) && Objects.equals(getDescricao(), transacao.getDescricao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValor(), getData(), getDescricao());
    }

    public void registrar () {};

    public void atualizarSaldo() {};
}
