package br.com.fintracker.repository.relatorioRepositories;

import br.com.fintracker.model.relatorio.RelatorioResumoCotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelatorioResumoCotasRepository extends JpaRepository <RelatorioResumoCotas, Long> {

    @Query(""" 
            SELECT t.categoria.nomeCategoria, SUM(t.valor) 
            FROM Transacao t 
            GROUP BY t.categoria.id   
            """
    )
    List<Object[]> calculaTotalGasto();
}
