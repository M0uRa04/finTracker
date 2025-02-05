package br.com.fintracker.model.relatorio;

import br.com.fintracker.model.categoria.Categoria;
import br.com.fintracker.model.categoria.StatusAtingimentoCota;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "relatorio_resumo_cotas")
public class RelatorioResumoCotas extends Relatorio {


    private Categoria categoria;
    private BigDecimal totalGasto;
    private Float porcentagemAtingimento;
    private StatusAtingimentoCota statusAtingimentoCota;

}
