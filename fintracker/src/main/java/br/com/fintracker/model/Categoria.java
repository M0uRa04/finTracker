package br.com.fintracker.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCategoria;
    private BigDecimal cota;
    private Boolean isAtivo;
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Transacao> transacoes;
    public Categoria () {
        this.isAtivo = true;
    };

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public BigDecimal getCota() {
        return cota;
    }

    public void setCota(BigDecimal cota) {
        this.cota = cota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(getId(), categoria.getId()) && Objects.equals(getNomeCategoria(), categoria.getNomeCategoria()) && Objects.equals(getCota(), categoria.getCota());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNomeCategoria(), getCota());
    }
}
