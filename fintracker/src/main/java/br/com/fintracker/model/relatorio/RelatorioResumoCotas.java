package br.com.fintracker.model.relatorio;

import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "relatorio_resumo_cotas")
public class RelatorioResumoCotas extends Relatorio {

    @OneToMany(mappedBy = "relatorioResumoCotas", fetch = FetchType.LAZY)
    private List<Categoria> categorias;

    private BigDecimal totalGasto;

    private Float porcentagemAtingimento;

    @Enumerated(value = EnumType.STRING)
    private StatusAtingimentoCota statusAtingimentoCota;

    public RelatorioResumoCotas () {}

    public RelatorioResumoCotas(List<Categoria> categorias, BigDecimal totalGasto, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        this.categorias = categorias;
        this.totalGasto = totalGasto;
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public RelatorioResumoCotas(Long id, TipoRelatorio tipoRelatorio, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, List<Categoria> categorias, BigDecimal totalGasto, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        super(id, tipoRelatorio, dataInicio, dataFim, usuario);
        this.categorias = categorias;
        this.totalGasto = totalGasto;
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public BigDecimal getTotalGasto() {
        return totalGasto;
    }

    public void setTotalGasto(BigDecimal totalGasto) {
        this.totalGasto = totalGasto;
    }

    public Float getPorcentagemAtingimento() {
        return porcentagemAtingimento;
    }

    public void setPorcentagemAtingimento(Float porcentagemAtingimento) {
        this.porcentagemAtingimento = porcentagemAtingimento;
    }

    public StatusAtingimentoCota getStatusAtingimentoCota() {
        return statusAtingimentoCota;
    }

    public void setStatusAtingimentoCota(StatusAtingimentoCota statusAtingimentoCota) {
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RelatorioResumoCotas that = (RelatorioResumoCotas) o;
        return Objects.equals(getCategorias(), that.getCategorias()) && Objects.equals(getTotalGasto(), that.getTotalGasto()) && Objects.equals(getPorcentagemAtingimento(), that.getPorcentagemAtingimento()) && getStatusAtingimentoCota() == that.getStatusAtingimentoCota();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategorias(), getTotalGasto(), getPorcentagemAtingimento(), getStatusAtingimentoCota());
    }

    @Override
    public String toString() {
        return "RelatorioResumoCotas{" +
                "categorias=" + categorias.stream()
                .map(c -> new DadosRespostaCategoria(c))
                .collect(Collectors.toList())+
                ", totalGasto=" + totalGasto +
                ", porcentagemAtingimento=" + porcentagemAtingimento +
                ", statusAtingimentoCota=" + statusAtingimentoCota +
                '}';
    }
}
