package br.com.fintracker.model.relatorio;

import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.RangeDatasRelatorioDTO;
import br.com.fintracker.dto.relatorio.relatorioatingimentocotas.TotalGastoPorCategoriaDTO;
import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import br.com.fintracker.model.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "relatorio_resumo_cotas")
public class RelatorioResumoCotas extends Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
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
        super(tipoRelatorio, dataInicio, dataFim, LocalDateTime.now(), usuario);
        this.categoria = categoria;
        this.totalGasto = totalGasto;
        this.porcentagemAtingimento = porcentagemAtingimento;
        this.statusAtingimentoCota = statusAtingimentoCota;
        this.id = id;
    }

    public RelatorioResumoCotas(TotalGastoPorCategoriaDTO totalGastoPorCategoriaDTO, RangeDatasRelatorioDTO rangeDatasRelatorioDTO, Usuario usuario, Float porcentagemAtingimento, StatusAtingimentoCota statusAtingimentoCota) {
        super(TipoRelatorio.PERSONALIZADO,
                rangeDatasRelatorioDTO.dataInicio(),
                rangeDatasRelatorioDTO.dataFim(),
                LocalDateTime.now(),
                usuario);
        this.categoria = totalGastoPorCategoriaDTO.categoria();
        this.totalGasto = totalGastoPorCategoriaDTO.totalGasto();
        this.porcentagemAtingimento = new BigDecimal(porcentagemAtingimento).setScale(2, RoundingMode.HALF_UP).floatValue();
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
