package br.com.fintracker.model.relatorio;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "relatorio_resumo_cotas")
public class RelatorioResumoCotas extends Relatorio {

    @OneToMany(mappedBy = "relatorioResumoCotas", fetch = FetchType.LAZY)
    private List<Categoria> categorias;

    private BigDecimal totalGasto;

    private Float porcentagemAtingimento;

    @Enumerated(value = EnumType.STRING)
    private StatusAtingimentoCota statusAtingimentoCota;

}
