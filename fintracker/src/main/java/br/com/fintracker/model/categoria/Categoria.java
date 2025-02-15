package br.com.fintracker.model.categoria;

import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "relatorio_resumo_cotas_id", nullable = true)
    private RelatorioResumoCotas relatorioResumoCotas;

    public Categoria () {
        this.isAtivo = true;
    };

    public Categoria(Long id, String nomeCategoria, BigDecimal cota, Boolean isAtivo, List<Transacao> transacoes, Usuario usuario) {
        this.id = id;
        this.nomeCategoria = nomeCategoria.toUpperCase();
        this.cota = cota;
        this.isAtivo = isAtivo;
        this.transacoes = transacoes;
        this.usuario = usuario;
    }

    public Categoria (DadosCadastroCategoria dados) {
        this.nomeCategoria = dados.nomeCategoria().toUpperCase();
        this.cota = dados.cota();
        this.isAtivo = true;
    }

    public Categoria(String nomeCategoria, BigDecimal cota, boolean isAtivo) {
        this.nomeCategoria = nomeCategoria;
        this.cota = cota;
        this.isAtivo = isAtivo;
    }

    public Categoria(String nomeCategoria, BigDecimal cota, boolean isAtivo, Usuario usuario) {
        this.nomeCategoria = nomeCategoria;
        this.cota = cota;
        this.isAtivo = isAtivo;
        this.usuario = usuario;
    }

    public Boolean getAtivo() {
        return isAtivo;
    }

    public void setAtivo(Boolean ativo) {
        isAtivo = ativo;
    }

    public void setId(Long id) {
        this.id = id;
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
        Categoria categoria = (Categoria) o;
        return Objects.equals(getId(), categoria.getId()) && Objects.equals(getNomeCategoria(), categoria.getNomeCategoria()) && Objects.equals(getCota(), categoria.getCota()) && Objects.equals(isAtivo, categoria.isAtivo) && Objects.equals(transacoes, categoria.transacoes) && Objects.equals(getUsuario(), categoria.getUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNomeCategoria(), getCota(), isAtivo, transacoes, getUsuario());
    }
}
