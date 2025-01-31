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
    private Float atingimentoCota;
    private BigDecimal totalGasto;
    @Enumerated(EnumType.STRING)
    private StatusAtingimentoCota statusAtingimentoCota;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Transacao> transacoes;

    @ManyToOne
    private Usuario usuario;

    public Categoria () {
        this.isAtivo = true;
        this.atingimentoCota = 0.0F;
        this.statusAtingimentoCota = StatusAtingimentoCota.DEFAULT;
        this.totalGasto = new BigDecimal("0.0");
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
        this.atingimentoCota = 0.0F;
        this.statusAtingimentoCota = StatusAtingimentoCota.DEFAULT;
        this.totalGasto = new BigDecimal("0.0");
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

    
    public Float getAtingimentoCota() {
        return atingimentoCota;
    }

    public void setAtingimentoCota(Float atingimentoCota) {
        this.atingimentoCota = atingimentoCota;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public StatusAtingimentoCota getStatusAtingimentoCota() {
        return statusAtingimentoCota;
    }

    public void setStatusAtingimentoCota(StatusAtingimentoCota statusAtingimentoCota) {
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public BigDecimal getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(BigDecimal valorTransacao) {
        this.totalGasto = totalGasto.add(totalGasto);
    }

    public void atualizaAtingimentoCota (BigDecimal valor) {
        this.setTotalGasto(valor);
        this.setAtingimentoCota(totalGasto.floatValue() / cota.floatValue() * 100);

        if (this.atingimentoCota > 100) {
            this.setStatusAtingimentoCota(StatusAtingimentoCota.EXCEDIDA);
        } else if (this.atingimentoCota == 100) {
            this.setStatusAtingimentoCota(StatusAtingimentoCota.ATINGIDA);
        } else if (this.atingimentoCota > 75 && this.atingimentoCota < 90) {
            this.setStatusAtingimentoCota(StatusAtingimentoCota.QUASE_ATINGIDA);
        } else {
            this.setStatusAtingimentoCota(StatusAtingimentoCota.DENTRO_DO_ESPERADO);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(getId(), categoria.getId()) && Objects.equals(getNomeCategoria(), categoria.getNomeCategoria()) && Objects.equals(getCota(), categoria.getCota()) && Objects.equals(isAtivo, categoria.isAtivo) && Objects.equals(getAtingimentoCota(), categoria.getAtingimentoCota()) && getStatusAtingimentoCota() == categoria.getStatusAtingimentoCota() && Objects.equals(transacoes, categoria.transacoes) && Objects.equals(getUsuario(), categoria.getUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNomeCategoria(), getCota(), isAtivo, getAtingimentoCota(), getStatusAtingimentoCota(), transacoes, getUsuario());
    }
}
