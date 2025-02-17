package br.com.fintracker.model.relatorio;

import br.com.fintracker.dto.categoria.DadosRespostaCategoria;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.RangeDatasRelatorioDTO;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "relatorio_resumo_cotas")
public class RelatorioResumoCotas extends Relatorio {

    @OneToOne(mappedBy = "relatorioResumoCotas", fetch = FetchType.LAZY)
    private Categoria categoria;

    private BigDecimal totalGasto;

    private Float porcentagemAtingimento;

    @Enumerated(value = EnumType.STRING)
    private StatusAtingimentoCota statusAtingimentoCota;

    public RelatorioResumoCotas () {}

    public RelatorioResumoCotas(Categoria categoria, BigDecimal totalGasto, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        this.categoria = categoria;
        this.totalGasto = totalGasto;
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public RelatorioResumoCotas(Long id, TipoRelatorio tipoRelatorio, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Categoria categoria, BigDecimal totalGasto, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        super(id, tipoRelatorio, dataInicio, dataFim, LocalDateTime.now(), usuario);
        this.categoria = categoria;
        this.totalGasto = totalGasto;
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
    }

    public RelatorioResumoCotas(TotalGastoPorCategoriaDTO totalGastoPorCategoriaDTO, RangeDatasRelatorioDTO rangeDatasRelatorioDTO, Long id, Usuario usuario, Categoria categoria, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        super(id, TipoRelatorio.PERSONALIZADO, rangeDatasRelatorioDTO.dataInicio(), rangeDatasRelatorioDTO.dataFim(), LocalDateTime.now(), usuario);
        this.categoria = totalGastoPorCategoriaDTO.categoria();
        this.totalGasto = totalGastoPorCategoriaDTO.totalGasto();
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
    }
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategorias(Categoria categoria) {
        this.categoria = categoria;
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
        return Objects.equals(getCategoria(), that.getCategoria()) && Objects.equals(getTotalGasto(), that.getTotalGasto()) && Objects.equals(getPorcentagemAtingimento(), that.getPorcentagemAtingimento()) && getStatusAtingimentoCota() == that.getStatusAtingimentoCota();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCategoria(), getTotalGasto(), getPorcentagemAtingimento(), getStatusAtingimentoCota());
    }

    @Override
    public String toString() {
        return "RelatorioResumoCotas{" +
                "categoria=" + categoria.getNomeCategoria() +
                ", totalGasto=" + totalGasto +
                ", porcentagemAtingimento=" + porcentagemAtingimento +
                ", statusAtingimentoCota=" + statusAtingimentoCota +
                '}';
    }
}
