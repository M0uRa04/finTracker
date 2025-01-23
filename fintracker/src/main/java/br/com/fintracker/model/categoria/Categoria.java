package br.com.fintracker.model.categoria;

import br.com.fintracker.dto.categoria.DadosCadastroCategoria;
import br.com.fintracker.model.transacao.Transacao;
import br.com.fintracker.model.usuario.Usuario;
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
    private BigDecimal atingimentoCota;
    private String statusAtingimentoCota;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Transacao> transacoes;

    @ManyToOne
    private Usuario usuario;

    public Categoria () {
        this.isAtivo = true;
        this.atingimentoCota = BigDecimal.valueOf(0.0);
        this.statusAtingimentoCota = "";
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
        this.atingimentoCota = BigDecimal.valueOf(0.0);
        this.statusAtingimentoCota = "";
    }

    public Categoria(String nomeCategoria, BigDecimal cota, boolean isAtivo) {
        this.nomeCategoria = nomeCategoria;
        this.cota = cota;
        this.isAtivo = isAtivo;
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

    
    public BigDecimal getAtingimentoCota() {
        return atingimentoCota;
    }

    public void setAtingimentoCota(BigDecimal atingimentoCota) {
        this.atingimentoCota = this.atingimentoCota.add(atingimentoCota);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getStatusAtingimentoCota() {
        return statusAtingimentoCota;
    }

    public void setStatusAtingimentoCota(String statusAtingimentoCota) {
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public void atualizaAtingimentoCota (BigDecimal valor) {
        this.setAtingimentoCota(valor);
        var porcentagemAtingimento = ((this.atingimentoCota.doubleValue() / this.cota.doubleValue()) * 100);

        if (porcentagemAtingimento > 100) {
            this.setStatusAtingimentoCota("excedida");
        } else if (porcentagemAtingimento == 100) {
            this.setStatusAtingimentoCota("atingida");
        } else if (porcentagemAtingimento > 75 && porcentagemAtingimento < 90) {
            this.setStatusAtingimentoCota("quase atingida");
        } else {
            this.setStatusAtingimentoCota("dentro do esperado");
        }
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
