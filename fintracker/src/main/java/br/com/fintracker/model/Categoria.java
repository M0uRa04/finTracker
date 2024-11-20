package br.com.fintracker.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Categoria {

    private Long id;
    private String nomeCategoria;
    private BigDecimal cota;
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Transacao> transacoes;

    public Categoria () {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
