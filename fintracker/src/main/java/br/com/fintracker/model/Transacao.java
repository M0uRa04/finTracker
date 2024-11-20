package br.com.fintracker.model;

import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table(name = "transacao")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valor;
    private LocalDateTime data;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;
    @ManyToOne
    private Categoria categoria;
    @ManyToOne
    private Usuario usuario;

    public Transacao(BigDecimal valor, LocalDateTime data, Categoria categoria, String descricao, TipoTransacao tipoTransacao, Usuario usuario) {
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.tipoTransacao = tipoTransacao;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(getId(), transacao.getId()) && Objects.equals(getValor(), transacao.getValor()) && Objects.equals(getData(), transacao.getData()) && Objects.equals(getCategoria(), transacao.getCategoria()) && Objects.equals(getDescricao(), transacao.getDescricao()) && getTipoTransacao() == transacao.getTipoTransacao() && Objects.equals(getUsuario(), transacao.getUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValor(), getData(), getCategoria(), getDescricao(), getTipoTransacao(), getUsuario());
    }

}
